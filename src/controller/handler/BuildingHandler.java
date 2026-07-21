package controller.handler;

import controller.SelectionContext;
import controller.handler.building.townhall.CookUnitHandler;
import model.state.GameState;
import model.map.Hex;
import model.Task;
import model.unit.Unit;
import model.building.BuildingFactory;
import model.building.TradingCenter;
import model.building.Wall;
import model.building.Road;
import model.building.Building;
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

        deductBuilderCost(building.getCost(), hex);
        hex.setBuilding(building);
        gameState.getEmpireState().getBuildings().add(building);
        gameState.getEmpireState().getBuildings().sort(Comparator.comparingInt(b -> b.getHex().getR()));
        afterBuildingChange();
    }

    public void updateTaskListPanel() {
        Hex selected = selectionContext.getSelectedHex();
        if (selected != null && selected.getBuilding() == null) {
            Map<Task.TASK, Boolean> canBuildMap = new HashMap<>();
            for (Task.TASK task : Task.getAvailableTasks(selected, gameState)) {
                boolean enoughAP = false;
                for (Unit unit : selected.getUnits()) {
                    if (unit.isBuilder()) {
                        enoughAP = Building.getBuildCost(task).enoughAP(unit.getActionPoints());
                    }
                }
                boolean canBuild = Building.getBuildCost(task).enoughResources(gameState.getEmpireState().getStorage())
                        && enoughAP
                        && selected.isInEmpireBorder();
                canBuildMap.put(task, canBuild);
            }
            List<TaskStat> stats = gamePanel.getTaskListPanel().updateTasks(canBuildMap);
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
        } else {
            gamePanel.getTaskListPanel().clear();
            if(selected.getBuilding() != null && !selected.getBuilding().isTownHall() && !(selected.getBuilding() instanceof TradingCenter)) {
                for(ActionListener al:gamePanel.getTaskListPanel().getDestroyButton().getActionListeners()) {
                    gamePanel.getTaskListPanel().getDestroyButton().removeActionListener(al);
                }
                gamePanel.getTaskListPanel().getDestroyButton().addActionListener(e -> {destroyBuilding(selected);});
                gamePanel.getTaskListPanel().showDestroyButton();
            }
        }
    }

    public void destroyBuilding(Hex hex) {
        for(Unit unit : hex.getUnits()) {
            if(unit.isBuilder() && unit.getActionPoints() >= 1) {
                int sure = JOptionPane.showConfirmDialog(null, "Are you sure ?", "Destroy", JOptionPane.YES_OPTION);
                if (sure == JOptionPane.YES_OPTION) {
                    Building building = hex.getBuilding();
                    if (building != null && !building.isTownHall()) {
                        gameState.getEmpireState().removeBuilding(building);
                        afterBuildingChange();
                    }
                }
                break;
            }
        }
    }

    public void startWallPlacement(Hex origin) {
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
    }

    public void startRoadPlacement(Hex origin) {
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
        gameState.getEmpireState().recalculateHappiness();
        gamePanel.getTaskListPanel().clear();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
    }
}
