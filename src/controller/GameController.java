package controller;

import controller.logics.CookUnit;
import controller.logics.EndTurn;
import helpers.SoundEffects;
import model.*;
import model.building.*;
import model.unit.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.awt.event.*;

public class GameController {
    private GamePanel gamePanel;
    private JPanel cards;
    private GameState gameState;

    public GameController(GamePanel gamePanel, JPanel cards, GameState gameState){
        this.gamePanel = gamePanel;
        this.cards = cards;
        this.gameState = gameState;
        this.attachHUDListeners();
        this.attachCameraListeners();
        this.attachHexSelectionListener();
        this.attachTownHallPanelListeners();
        gamePanel.getMapPanel().setOnUnitArrived(this::onArrive);
    }

    private void attachHUDListeners() {

        gamePanel.getHudPanel().getMenuButton().addActionListener(e -> menuAction());
        gamePanel.getHudPanel().getEndTurnButton().addActionListener(e -> endTurnAction());

    }

    private void attachTownHallPanelListeners(){
        gamePanel.getTownHallPanel().getCloseButton().addActionListener(e -> gamePanel.getTownHallPanel().close());
        for(Unit.UNIT_TYPE unitType:Unit.UNIT_TYPE.values()){
            gamePanel.getTownHallPanel().getCookButton(unitType).addActionListener(e-> {
                CookUnit.startCook(gameState, unitType);
                gamePanel.getTownHallPanel().refresh();
            });
        }
        gamePanel.getTownHallPanel().getUpgradeStorageButton().addActionListener(e-> {
            gameState.setStorageLevel(gameState.getStorageLevel()+1);
            gameState.getStorage().upgradeStorage(gameState.getStorageLevel());
            updateTaskListPanel();
            gamePanel.getTownHallPanel().refresh();
        });
    }

    private void attachCameraListeners(){
        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "zoomIn");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "zoomOut");

        actionMap.put("zoomIn",  new AbstractAction() {
            public void actionPerformed(ActionEvent e) { gamePanel.getMapPanel().zoom(1); }
        });
        actionMap.put("zoomOut", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { gamePanel.getMapPanel().zoom(-1); }
        });

        actionMap.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.getMapPanel().moveCamera(0, -50);
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.getMapPanel().moveCamera(0, 50);
            }
        });
        actionMap.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.getMapPanel().moveCamera(-50, 0);
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.getMapPanel().moveCamera(50, 0);
            }
        });
    }

    private void attachHexSelectionListener() {
        MapPanel mapPanel = gamePanel.getMapPanel();
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Hex clicked = mapPanel.getHexAt(e.getX(), e.getY());
                boolean discoveredNeighbour = false;
                for(Hex hex:gameState.getMap().getNeighbourHexes(clicked)){
                    if (hex.isDiscovered()) {
                        discoveredNeighbour = true;
                        break;
                    }
                }
                if(gameState.getSelectedUnit()!=null){
                    handleUnitMoving(clicked);
                }else{
                    if (discoveredNeighbour) {
                        if (gameState.getSelectedHex()==clicked && clicked.getBuilding() instanceof TownHall){
                            gamePanel.getTownHallPanel().open();
                        }

                        gameState.setSelectedHex(clicked);
                        SoundEffects.getInstance().playSelectHexSoundEffect();

                        updateUnitListPanel();
                        updateTaskListPanel();
                    }
                }
            }
        });
    }

    private void updateUnitListPanel() {
        Hex selected = gameState.getSelectedHex();
        if (selected != null) {
            List<UnitStat> stats = gamePanel.getUnitListPanel().updateUnits(gameState.getSelectedHex().getUnits());
            for (UnitStat stat:stats){
                stat.getButton().addActionListener(e -> {
                    gameState.setSelectedUnit(stat.getUnit());
                    gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                });
            }
            gamePanel.getTaskListPanel().clear();
        } else {
            gamePanel.getUnitListPanel().clear();
        }
    }


    private void updateTaskListPanel() {
        Hex selected = gameState.getSelectedHex();
        if (selected != null && selected.getBuilding() == null) {
            List<TaskStat> stats = gamePanel.getTaskListPanel().updateTasks(Task.getAvailableTasks(selected), gameState);
            for (TaskStat stat:stats){
                stat.getButton().addActionListener(e -> handleBuildingTask(stat.getTask(), selected));
            }
        } else {
            gamePanel.getTaskListPanel().clear();
        }
    }


    private void menuAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }


    private void endTurnAction(){

        for(Unit unit:gameState.getUnits()){
            if(unit.getActionPoints()!=0){
                int x = WarningDialogs.unitsWithActionPoints(gamePanel);
                if(x==JOptionPane.NO_OPTION){
                    return;
                }else{
                    break;
                }
            }
        }
        EndTurn.endTurn(gameState);
        CookUnit.checkCooks(gameState);
        gamePanel.getTownHallPanel().refresh();
        updateLists();
        if(gameState.isUpkeepDisaster()){
            WarningDialogs.upkeepDisaster(gamePanel);
        }
        if(gameState.isStarving()){
            WarningDialogs.starvationDisaster(gamePanel);
        }

    }


    private void handleBuildingTask(Task.TASK task, Hex hex) {
        Building building = switch (task) {
            case FARM        -> new Farm(hex, Hex.ResourceType.WHEAT);
            case LUMBER_MILL -> new LumberMill(hex);
            case STONE_MINE  -> new StoneMine(hex);
            case IRON_MINE   -> new IronMine(hex);
            case STABLE      -> new Stable(hex, Hex.ResourceType.COW);
            case TOWN        -> new Town(hex);
        };

        building.getCost().pay(gameState.getStorage());
        List<Unit> toRemove = new ArrayList<>();
        for(Unit unit:hex.getUnits()){
            if(unit instanceof Builder && unit.getActionPoints()>=building.getCost().getAPNeeded()) {
                unit.setActionPoints(unit.getActionPoints() - building.getCost().getAPNeeded());
                ((Builder) unit).setCharges(((Builder) unit).getCharges()-1);
                if(((Builder) unit).getCharges()==0){
                    gameState.getUnits().remove(unit);
                    toRemove.add(unit);
                }
                updateUnitListPanel();
                break;
            }
        }
        hex.getUnits().removeAll(toRemove);
        hex.setBuilding(building);
        gameState.getBuildings().add(building);
        gameState.getBuildings().sort(Comparator.comparingInt(b -> b.getHex().getR()));
        gameState.updateTownCapacity();
        gameState.getProduction().recalculate(gameState);
        gamePanel.getTaskListPanel().clear();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().repaint();
    }

    private void handleUnitMoving(Hex clicked){
        Unit unit = gameState.getSelectedUnit();
        if(gameState.getSelectedHex()==clicked) {
            if (unit instanceof Worker) {
                if (gameState.getSelectedHex().getBuilding() != null && !(gameState.getSelectedHex().getBuilding() instanceof Town)) {
                    if (!unit.isAssigned()) {
                        if(gameState.getSelectedHex().getBuilding().getAssignedWorkers().size()<gameState.getSelectedHex().getBuilding().getWorkerCapacity()){
                            if (unit.getActionPoints() >= Building.actionPointToAssign) {
                                unit.setAssigned(true);
                                unit.setActionPoints(unit.getActionPoints() - Building.actionPointToAssign);
                                gameState.getSelectedHex().getBuilding().getAssignedWorkers().add((Worker) unit);
                                gameState.getProduction().recalculate(gameState);
                                gamePanel.getHudPanel().refresh();
                            }
                        }
                    } else {
                        unit.setAssigned(false);
                        gameState.getSelectedHex().getBuilding().getAssignedWorkers().remove(unit);
                        gameState.getProduction().recalculate(gameState);
                        gamePanel.getHudPanel().refresh();
                    }
                }
            } else if (unit instanceof BorderExpander) {
                for (Hex neighbor:gameState.getMap().getNeighbourHexes(gameState.getSelectedHex())){
                    if (!neighbor.isInEmpireBorder()){
                        neighbor.setInEmpireBorder(true);
                    }
                }
                clicked.setInEmpireBorder(true);
                gameState.getSelectedHex().getUnits().remove(unit);
                gameState.getUnits().remove(unit);
                updateLists();
            }
        }


        if(gameState.getMap().getNeighbourHexes(gameState.getSelectedHex()).contains(clicked)){
            if(unit.getActionPoints()>=clicked.getAPToMove()) {

                if(unit.getHex().getBuilding()!=null){
                    unit.getHex().getBuilding().getAssignedWorkers().remove(unit);
                    gameState.getProduction().recalculate(gameState);
                    gamePanel.getHudPanel().refresh();
                }

                unit.setActionPoints(unit.getActionPoints()-clicked.getAPToMove());
                unit.setTargetHex(clicked);
                unit.setMoving(true);
                unit.setAssigned(false);
            }
        }

        gameState.setSelectedUnit(null);
        gameState.setSelectedHex(clicked);
        gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        updateUnitListPanel();
        updateTaskListPanel();
    }

    public void updateLists(){
        updateUnitListPanel();
        updateTaskListPanel();
    }

    public void discover(){
        for(Unit unit:gameState.getSelectedHex().getUnits()){
            if(unit instanceof Explorer){
                for(Hex neighbor:gameState.getMap().getNeighbourHexes(gameState.getSelectedHex())){
                    neighbor.setDiscovered(true);
                }
            }
        }
    }

    public void onArrive(){
        updateLists();
        discover();
    }

}