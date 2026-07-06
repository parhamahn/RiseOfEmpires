package controller.handler;

import model.GameState;
import model.Hex;
import model.Task;
import model.unit.Unit;
import model.building.*;
import view.GamePanel;
import view.TaskStat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BuildingHandler {
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final CookUnitHandler cookUnitHandler;

    public BuildingHandler(GameState gameState, GamePanel gamePanel, CookUnitHandler cookUnitHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.cookUnitHandler = cookUnitHandler;
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
            gameState.setStorageLevel(gameState.getStorageLevel() + 1);
            gameState.getStorage().upgradeStorage(gameState.getStorageLevel());
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

        building.getCost().pay(gameState.getStorage());
        List<Unit> toRemove = new ArrayList<>();
        for (Unit unit : hex.getUnits()) {
            if (unit.isBuilder() && unit.getActionPoints() >= building.getCost().getAPNeeded()) {
                unit.setActionPoints(unit.getActionPoints() - building.getCost().getAPNeeded());
                unit.setCharges(unit.getCharges() - 1);
                if (unit.getCharges() == 0) {
                    gameState.getUnits().remove(unit);
                    toRemove.add(unit);
                }
                break;
            }
        }
        hex.getUnits().removeAll(toRemove);
        hex.setBuilding(building);
        gameState.getBuildings().add(building);
        gameState.getBuildings().sort(Comparator.comparingInt(b -> b.getHex().getR()));
        updateTownCapacity();
        gameState.getProduction().recalculate(gameState);
        gamePanel.getTaskListPanel().clear();
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().repaint();
    }

    public void updateTaskListPanel() {
        Hex selected = gameState.getSelectedHex();
        if (selected != null && selected.getBuilding() == null) {
            List<TaskStat> stats = gamePanel.getTaskListPanel().updateTasks(Task.getAvailableTasks(selected), gameState);
            for (TaskStat stat : stats) {
                stat.getButton().addActionListener(e -> handleBuildingTask(stat.getTask(), selected));
            }
        } else {
            gamePanel.getTaskListPanel().clear();
        }
    }

    public void updateTownCapacity(){
        int townCount = 0;
        for (Building building : gameState.getBuildings()) {
            if (building.isTown()) {
                townCount++;
            }
        }
        gameState.setMaxUnit(GameState.DEFAULT_UNIT_CAPACITY + townCount);
    }
}
