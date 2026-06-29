package controller;

import controller.logics.EndTurn;
import helpers.SoundEffects;
import model.*;
import model.building.*;
import view.GamePanel;
import view.MapPanel;
import view.TaskStat;
import view.UnitStat;

import javax.swing.*;
import java.awt.*;
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
    }

    private void attachHUDListeners() {

        gamePanel.getHudPanel().getMenuButton().addActionListener(e -> menuAction());
        gamePanel.getHudPanel().getEndTurnButton().addActionListener(e -> endTurnAction());

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
                    if (clicked != null && discoveredNeighbour) {
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

        EndTurn.endTurn(gameState);
        updateTaskListPanel();

    }


    private void handleBuildingTask(Task.TASK task, Hex hex) {
        Building building = switch (task) {
            case FARM        -> new Farm(hex);
            case LUMBER_MILL -> new LumberMill(hex);
            case STONE_MINE  -> new StoneMine(hex);
            case IRON_MINE   -> new IronMine(hex);
            case STABLE      -> new Stable(hex);
            case TOWN        -> new Town(hex);
        };

        if (building.getCost().enoughResources(gameState.getStorage())) {
            building.getCost().pay(gameState.getStorage());
            hex.setBuilding(building);
            gameState.getBuildings().add(building);
            gameState.getBuildings().sort(Comparator.comparingInt(b -> b.getHex().getR()));
        }

        gameState.getProduction().recalculate(gameState);
        gamePanel.getTaskListPanel().clear();
        gameState.updateUnits();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().repaint();
    }

    private void handleUnitMoving(Hex clicked){
        gameState.getSelectedUnit().setHex(clicked);
        clicked.getUnits().add(gameState.getSelectedUnit());
        gameState.getSelectedHex().getUnits().remove(gameState.getSelectedUnit());
        gameState.setSelectedUnit(null);
        gameState.setSelectedHex(clicked);
        gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        updateUnitListPanel();
        updateTaskListPanel();
    }


}