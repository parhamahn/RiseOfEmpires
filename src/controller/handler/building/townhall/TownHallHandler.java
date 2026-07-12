package controller.handler.building.townhall;

import controller.handler.BuildingHandler;
import model.state.GameState;
import view.game.GamePanel;

public class TownHallHandler {

    private final GameState gameState;
    private final GamePanel gamePanel;
    private final BuildingHandler buildingHandler;
    private final CookUnitHandler cookUnitHandler;

    public TownHallHandler(GameState gameState, GamePanel gamePanel, BuildingHandler buildingHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.buildingHandler = buildingHandler;
        this.cookUnitHandler = new CookUnitHandler();
    }

    public void attachHandlers() {
        gamePanel.getTownHallPanel().getCloseButton().addActionListener(e -> {gamePanel.setAnyPanelOpen(false); gamePanel.getTownHallPanel().close();});

        UpgradeHandler upgradeHandler = new UpgradeHandler(gameState, gamePanel, cookUnitHandler, buildingHandler);
        upgradeHandler.attachUpgradeListeners();

        ResearchHandler researchHandler = new ResearchHandler(gameState, gamePanel);
        researchHandler.attachResearchListeners();
    }
}
