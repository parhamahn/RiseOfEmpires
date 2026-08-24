package controller.handler;

import controller.SelectionContext;
import controller.handler.building.townhall.CookUnitHandler;
import helpers.sound.SoundEffects;
import model.building.tribes.Tribe;
import model.state.GameState;
import model.map.Hex;
import model.Task;
import model.unit.Unit;
import model.building.BuildingFactory;
import model.building.other.TradingCenter;
import model.building.edge.Wall;
import model.building.edge.Road;
import model.building.Building;
import model.building.empire.Dock;
import model.storage.Cost;
import view.game.GamePanel;
import view.game.lists.TaskStat;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.*;

public class BuildingHandler {
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final CookUnitHandler cookUnitHandler;
    private final SelectionContext selectionContext;

    public BuildingHandler(GameState gameState, GamePanel gamePanel, CookUnitHandler cookUnitHandler, SelectionContext selectionContext) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.cookUnitHandler = cookUnitHandler;
        this.selectionContext = selectionContext;
    }

    public void handleBuildingTask(Task.TASK task, Hex hex) {
        if (hex.getBuilding() != null) return;

        Building building = BuildingFactory.createBuilding(task, hex);
        building.setOwnerID(gameState.resolveActivePlayerId());

        Cost cost = building.getCost();
        if (building instanceof Dock && gameState.getEmpireState().isDockDiscount()) {
            gameState.getEmpireState().setDockDiscount(false);
            cost = new Cost(cost.getWoodNeeded()/2, cost.getStoneNeeded()/2, cost.getIronNeeded()/2, cost.getFoodNeeded()/2, cost.getAPNeeded());
        }
        if (!cost.enoughResources(gameState.getEmpireState().getStorage())) return;
        if (!hasBuilderWithAP(hex, cost.getAPNeeded())) return;
        deductBuilderCost(cost, hex);
        hex.setBuilding(building);
        gameState.getEmpireState().getBuildings().add(building);
        if (building.isTown()) {
            gameState.getEmpireState().addHappiness(-1);
        }
        gameState.getEmpireState().getBuildings().sort(Comparator.comparingInt(b -> b.getHex().getR()));
        afterBuildingChange();
        //SoundEffects.getInstance().playConstructSoundEffect();
    }

    private boolean hasBuilderWithAP(Hex hex, int apNeeded) {
        return hex.getUnits().stream().anyMatch(unit -> unit.isBuilder() && unit.getActionPoints() >= apNeeded);
    }

    public void updateTaskListPanel() {
        Hex selected = selectionContext.getSelectedHex();
        if (selected != null && selected.getUnits().stream().anyMatch(Unit::isBuilder)) {
            Map<Task.TASK, String> blockReasonMap = new HashMap<>();
            for (Task.TASK task : Task.getAvailableTasks(selected, gameState)) {
                blockReasonMap.put(task, Task.getBuildBlockReason(task, selected, gameState));
            }
            List<TaskStat> stats = gamePanel.getTaskListPanel().updateTasks(blockReasonMap);
            for (TaskStat stat : stats) {
                stat.getButton().addActionListener(e -> {
                    if (stat.getTask() == Task.TASK.WALL) {
                        startWallPlacement(selected);
                    } else if (stat.getTask() == Task.TASK.ROAD) {
                        startRoadPlacement(selected);
                    } else {
                        handleBuildingTask(stat.getTask(), selected);
                    }
                });
            }
            if((selected.getBuilding() != null && !selected.getBuilding().isTownHall() && !(selected.getBuilding() instanceof TradingCenter || selected.getBuilding() instanceof Tribe)) && selected.getUnits().stream().anyMatch(Unit::isBuilder)) {
                for(ActionListener al:gamePanel.getTaskListPanel().getDestroyButton().getActionListeners()) {
                    gamePanel.getTaskListPanel().getDestroyButton().removeActionListener(al);
                }
                gamePanel.getTaskListPanel().getDestroyButton().addActionListener(e -> {destroyBuilding(selected);});
                gamePanel.getTaskListPanel().showDestroyButton();
            }
            if (!selected.getWallsWith().isEmpty()) {
                for(ActionListener al:gamePanel.getTaskListPanel().getWallDestroyButton().getActionListeners()) {
                    gamePanel.getTaskListPanel().getWallDestroyButton().removeActionListener(al);
                }
                gamePanel.getTaskListPanel().getWallDestroyButton().addActionListener(e -> {startWallDestroy(selected);});
                gamePanel.getTaskListPanel().showWallDestroyButton();
            }
            if (!selected.getRoadsTo().isEmpty()) {
                for(ActionListener al:gamePanel.getTaskListPanel().getRoadDestroyButton().getActionListeners()) {
                    gamePanel.getTaskListPanel().getRoadDestroyButton().removeActionListener(al);
                }
                gamePanel.getTaskListPanel().getRoadDestroyButton().addActionListener(e -> {startRoadDestroy(selected);});
                gamePanel.getTaskListPanel().showRoadDestroyButton();
            }
        }
    }

    public void destroyBuilding(Hex hex) {
        for(Unit unit : hex.getUnits()) {
            if(unit.isBuilder() && unit.getActionPoints() >= 1) {
                int sure = JOptionPane.showConfirmDialog(null, "Are you sure ?", "Destroy", JOptionPane.YES_NO_OPTION);
                if (sure == JOptionPane.YES_OPTION && hex.getBuilding() instanceof Building building && !building.isTownHall()) {
                    gameState.getEmpireState().removeBuilding(building);
                    unit.setActionPoints(unit.getActionPoints() - 1);
                    afterBuildingChange();
                }
                break;
            }
        }
    }

    public void startWallPlacement(Hex origin) {
        selectionContext.clearAllModes();
        selectionContext.setSelectedUnit(null);
        selectionContext.setWallOriginHex(origin);
        gamePanel.getTaskListPanel().clear();
        gamePanel.getMapPanel().repaint();
    }

    public void handleWallTask(Hex origin, Hex edgeHex) {
        model.map.Map map = gameState.getWorldState().getMap();

        if (edgeHex == null || !map.getNeighbourHexes(origin).contains(edgeHex)
                || origin.hasWallTo(edgeHex)) {
            selectionContext.setWallOriginHex(null);
            return;
        }

        Wall wall = new Wall(origin, edgeHex, map);
        if (!wall.isValid()) {
            selectionContext.setWallOriginHex(null);
            return;
        }

        deductBuilderCost(Wall.getBuildCost(), origin);

        origin.getWallsWith().add(wall);
        edgeHex.getWallsWith().add(wall);

        selectionContext.setWallOriginHex(null);
        afterBuildingChange();
        //SoundEffects.getInstance().playConstructSoundEffect();
    }

    public void startRoadPlacement(Hex origin) {
        selectionContext.clearAllModes();
        selectionContext.setSelectedUnit(null);
        selectionContext.setRoadOriginHex(origin);
        gamePanel.getTaskListPanel().clear();
        gamePanel.getMapPanel().repaint();
    }

    public void handleRoadTask(Hex origin, Hex edgeHex) {
        model.map.Map map = gameState.getWorldState().getMap();

        if (edgeHex == null || !map.getNeighbourHexes(origin).contains(edgeHex)
                || origin.hasRoadTo(edgeHex)) {
            selectionContext.setRoadOriginHex(null);
            return;
        }

        Road road = new Road(origin, edgeHex, map);
        if (!road.isValid()) {
            selectionContext.setRoadOriginHex(null);
            return;
        }

        deductBuilderCost(Road.getBuildCost(), origin);

        origin.getRoadsTo().add(road);
        edgeHex.getRoadsTo().add(road);

        selectionContext.setRoadOriginHex(null);
        afterBuildingChange();
        //SoundEffects.getInstance().playConstructSoundEffect();
    }

    public void startWallDestroy(Hex origin) {
        selectionContext.clearAllModes();
        selectionContext.setSelectedUnit(null);
        selectionContext.setDestroyWallOriginHex(origin);
        gamePanel.getTaskListPanel().clear();
        gamePanel.getMapPanel().repaint();
    }

    public void handleWallDestroy(Hex origin, Hex edgeHex) {
        if (edgeHex == null || !origin.hasWallTo(edgeHex)) {
            selectionContext.setDestroyWallOriginHex(null);
            return;
        }

        Wall wallToRemove = null;
        for (Wall wall : origin.getWallsWith()) {
            if (wall.connects(origin, edgeHex)) {
                wallToRemove = wall;
                break;
            }
        }

        if (wallToRemove != null) {
            deductBuilderCost(new model.storage.Cost(0, 0, 0, 0, 1), origin);
            origin.getWallsWith().remove(wallToRemove);
            edgeHex.getWallsWith().remove(wallToRemove);
        }

        selectionContext.setDestroyWallOriginHex(null);
        afterBuildingChange();
    }

    public void startRoadDestroy(Hex origin) {
        selectionContext.clearAllModes();
        selectionContext.setSelectedUnit(null);
        selectionContext.setDestroyRoadOriginHex(origin);
        gamePanel.getTaskListPanel().clear();
        gamePanel.getMapPanel().repaint();
    }

    public void handleRoadDestroy(Hex origin, Hex edgeHex) {
        boolean anyBuilder = origin.getUnits().stream().anyMatch(Unit::isBuilder);
        if(!anyBuilder) {return;}
        if (edgeHex == null || !origin.hasRoadTo(edgeHex)) {
            selectionContext.setDestroyRoadOriginHex(null);
            return;
        }

        Road roadToRemove = null;
        for (Road road : origin.getRoadsTo()) {
            if (road.connects(origin, edgeHex)) {
                roadToRemove = road;
                break;
            }
        }

        if (roadToRemove != null) {
            deductBuilderCost(new model.storage.Cost(0, 0, 0, 0, 1), origin);
            origin.getRoadsTo().remove(roadToRemove);
            edgeHex.getRoadsTo().remove(roadToRemove);
        }

        selectionContext.setDestroyRoadOriginHex(null);
        afterBuildingChange();
    }

    private void deductBuilderCost(model.storage.Cost cost, Hex hex) {
        cost.pay(gameState.getEmpireState().getStorage());
        for (Unit unit : hex.getUnits()) {
            if (unit.isBuilder() && unit.getActionPoints() >= cost.getAPNeeded()) {
                unit.setActionPoints(unit.getActionPoints() - cost.getAPNeeded());
                unit.setCharges(unit.getCharges() - 1);
                if (unit.getCharges() == 0) {
                    gameState.getEmpireState().removeUnit(unit);
                }
                break;
            }
        }
    }

    private void afterBuildingChange() {
        gameState.getEmpireState().updateTownCapacity();
        gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
        gamePanel.getTaskListPanel().clear();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
    }
}
