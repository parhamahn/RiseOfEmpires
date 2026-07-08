package controller.handler;

import controller.SelectionContext;
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

    public void attachTownHallPanelListeners() {
        gamePanel.getTownHallPanel().getCloseButton().addActionListener(e -> gamePanel.getTownHallPanel().close());
        for (Unit.UNIT_TYPE unitType : Unit.UNIT_TYPE.values()) {
            gamePanel.getTownHallPanel().getCookButton(unitType).addActionListener(e -> {
                cookUnitHandler.startCook(gameState, unitType);
                gamePanel.getTownHallPanel().refresh();
            });
        }
        gamePanel.getTownHallPanel().getUpgradeStorageButton().addActionListener(e -> {
            TownHallLevel next = gameState.getEmpireState().getTownHallLevel().nextUpgrade();
            if (next != null) {
                gameState.getEmpireState().getQueues().add(
                        new UpgradeQueue(next, next.getNextUpgradeTurnCost(),
                                gameState.getWorldState().getTurnNumber()));
            }
            updateTaskListPanel();
            gamePanel.getTownHallPanel().refresh();
        });
    }

    public void handleBuildingTask(Task.TASK task, Hex hex) {
        Building building = switch (task) {
            case FARM -> new Farm(hex, hex.getResources().containsKey(Hex.ResourceType.WHEAT) ? Hex.ResourceType.WHEAT : Hex.ResourceType.RICE);
            case LUMBER_MILL -> new LumberMill(hex);
            case STONE_MINE -> new StoneMine(hex);
            case IRON_MINE -> new IronMine(hex);
            case STABLE -> new Stable(hex, Hex.ResourceType.COW);
            case TOWN -> new Town(hex);
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
        gameState.getEmpireState().recalculateProduction();
        gamePanel.getTaskListPanel().clear();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().repaint();
    }

    public void updateTaskListPanel() {
        Hex selected = selectionContext.getSelectedHex();
        if (selected != null && selected.getBuilding() == null) {
            Map<Task.TASK, Boolean> canBuildMap = new HashMap<>();
            for (Task.TASK task : Task.getAvailableTasks(selected)) {
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
                stat.getButton().addActionListener(e -> handleBuildingTask(stat.getTask(), selected));
            }
        } else {
            gamePanel.getTaskListPanel().clear();
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
}
