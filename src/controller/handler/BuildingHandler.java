package controller.handler;

import controller.SelectionContext;
import controller.handler.building.townhall.CookUnitHandler;
import model.TownHallLevel;
import model.state.EmpireState;
import model.state.GameState;
import model.map.Hex;
import model.Task;
import model.unit.Unit;
import model.building.*;
import model.queue.UpgradeQueue;
import view.game.GamePanel;
import view.game.lists.TaskStat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

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

        Building building = switch (task) {
            case FARM -> new Farm(hex, hex.getResources().containsKey(Hex.ResourceType.WHEAT) ? Hex.ResourceType.WHEAT : Hex.ResourceType.RICE);
            case LUMBER_MILL -> new LumberMill(hex);
            case STONE_MINE -> new StoneMine(hex);
            case IRON_MINE -> new IronMine(hex);
            case STABLE -> new Stable(hex, Hex.ResourceType.COW);
            case TOWN -> new Town(hex);
            case MONUMENT -> new Monument(hex);
            case BAZAAR -> new Bazaar(hex);
            case DOCK ->  new Dock(hex);
            case MILITARY_STABLE ->  new MilitaryStable(hex);
            case WALL -> null;
            case ROAD -> null;
        };

        building.getCost().pay(gameState.getEmpireState().getStorage());
        List<Unit> toRemove = new ArrayList<>();
        for (Unit unit : hex.getUnits()) {
            if (unit.isBuilder() && unit.getActionPoints() >= building.getCost().getAPNeeded()) {
                unit.setActionPoints(unit.getActionPoints() - building.getCost().getAPNeeded());
                unit.setCharges(unit.getCharges() - 1);
                if (unit.getCharges() == 0) {
                    gameState.getEmpireState().getUnits().remove(unit);
                    toRemove.add(unit);
                }
                break;
            }
        }
        hex.getUnits().removeAll(toRemove);
        hex.setBuilding(building);
        gameState.getEmpireState().getBuildings().add(building);
        gameState.getEmpireState().getBuildings().sort(Comparator.comparingInt(b -> b.getHex().getR()));
        updateTownCapacity();
        gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
        gameState.getEmpireState().recalculateHappiness();
        gamePanel.getTaskListPanel().clear();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
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
                        for (Unit tunit : building.getAssignedWorkers()) {
                            tunit.setAssigned(false);
                        }
                        hex.setBuilding(null);
                        gameState.getEmpireState().getBuildings().remove(building);
                        updateTownCapacity();
                        gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
                        gameState.getEmpireState().recalculateHappiness();
                        gamePanel.getTaskListPanel().clear();
                        gamePanel.getHudPanel().refresh();
                        gamePanel.getMapPanel().markStaticDirty();
                        gamePanel.getMapPanel().repaint();
                    }
                }
                break;
            }
        }
    }

    public void updateTownCapacity(){
        int townCount = 0;
        for (Building building : gameState.getEmpireState().getBuildings()) {
            if (building.isTown()) {
                townCount++;
            }
        }
        gameState.getEmpireState().setMaxUnit(EmpireState.DEFAULT_UNIT_CAPACITY + townCount);
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
                || gameState.getEmpireState().hasWall(origin, edgeHex)) {
            selectionContext.setWallOriginHex(null);
            return;
        }

        Wall wall = new Wall(origin, edgeHex, map);
        if (!wall.isValid()) {
            selectionContext.setWallOriginHex(null);
            return;
        }

        Wall.getBuildCost().pay(gameState.getEmpireState().getStorage());
        List<Unit> toRemove = new ArrayList<>();
        for (Unit unit : origin.getUnits()) {
            if (unit.isBuilder() && unit.getActionPoints() >= Wall.getBuildCost().getAPNeeded()) {
                unit.setActionPoints(unit.getActionPoints() - Wall.getBuildCost().getAPNeeded());
                unit.setCharges(unit.getCharges() - 1);
                if (unit.getCharges() == 0) {
                    gameState.getEmpireState().getUnits().remove(unit);
                    toRemove.add(unit);
                }
                break;
            }
        }
        origin.getUnits().removeAll(toRemove);

        gameState.getEmpireState().getWalls().add(wall);

        selectionContext.setWallOriginHex(null);
        gamePanel.getTaskListPanel().clear();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
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
                || gameState.getEmpireState().hasRoad(origin, edgeHex)) {
            selectionContext.setRoadOriginHex(null);
            return;
        }

        Road road = new Road(origin, edgeHex, map);
        if (!road.isValid()) {
            selectionContext.setRoadOriginHex(null);
            return;
        }

        Road.getBuildCost().pay(gameState.getEmpireState().getStorage());
        List<Unit> toRemove = new ArrayList<>();
        for (Unit unit : origin.getUnits()) {
            if (unit.isBuilder() && unit.getActionPoints() >= Road.getBuildCost().getAPNeeded()) {
                unit.setActionPoints(unit.getActionPoints() - Road.getBuildCost().getAPNeeded());
                unit.setCharges(unit.getCharges() - 1);
                if (unit.getCharges() == 0) {
                    gameState.getEmpireState().getUnits().remove(unit);
                    toRemove.add(unit);
                }
                break;
            }
        }
        origin.getUnits().removeAll(toRemove);

        gameState.getEmpireState().getRoads().add(road);

        selectionContext.setRoadOriginHex(null);
        gamePanel.getTaskListPanel().clear();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
    }
}
