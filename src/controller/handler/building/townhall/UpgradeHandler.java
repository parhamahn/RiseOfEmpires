package controller.handler.building.townhall;

import controller.handler.BuildingHandler;
import model.TownHallLevel;
import model.queue.UpgradeQueue;
import model.state.GameState;
import model.unit.Unit;
import view.game.GamePanel;

public class UpgradeHandler {

    private final GameState gameState;
    private final GamePanel gamePanel;
    private final CookUnitHandler cookUnitHandler;
    private final BuildingHandler buildingHandler;

    public UpgradeHandler(GameState gameState, GamePanel gamePanel, CookUnitHandler cookUnitHandler, BuildingHandler buildingHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.cookUnitHandler = cookUnitHandler;
        this.buildingHandler = buildingHandler;
    }

    public void attachUpgradeListeners() {
        for (Unit.UNIT_TYPE unitType : Unit.UNIT_TYPE.values()) {
            if(unitType == Unit.UNIT_TYPE.BEAR || unitType== Unit.UNIT_TYPE.BARBARIAN || unitType == Unit.UNIT_TYPE.GUARD){continue;}
            gamePanel.getTownHallPanel().getCookButton(unitType).addActionListener(e -> {
                cookUnitHandler.startCook(gameState, unitType);
                gamePanel.getTownHallPanel().refresh();
            });
        }
        gamePanel.getTownHallPanel().getUpgradeStorageButton().addActionListener(e -> {
            if (gameState.getEmpireState().canUpgrade()) {
                TownHallLevel next = gameState.getEmpireState().getTownHallLevel().nextUpgrade();
                gameState.getEmpireState().getQueues().add(
                        new UpgradeQueue(next, next.getNextUpgradeTurnCost(),
                                gameState.getWorldState().getTurnNumber()));
                next.getCost().pay(gameState.getEmpireState().getStorage());
                gamePanel.getHudPanel().refresh();
            }
             buildingHandler.updateTaskListPanel();
            gamePanel.getTownHallPanel().refresh();
        });
    }

}
