package controller.handler.building.bazaar;

import view.game.GamePanel;

public class BazaarHandler {

    private final GamePanel gamePanel;

    public BazaarHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void attachHandlers() {
        gamePanel.getBazaarPanel().getCloseButton().addActionListener(e -> gamePanel.getBazaarPanel().close());
    }
}
