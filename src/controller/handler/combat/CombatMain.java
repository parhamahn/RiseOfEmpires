package controller.handler.combat;

import controller.SelectionContext;
import model.state.GameState;
import view.game.GamePanel;

public class CombatMain {

    private GameState gameState;
    private GamePanel gamePanel;
    private SelectionContext selectionContext;

    public CombatMain(GameState gameState, GamePanel gamePanel, SelectionContext selectionContext) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.selectionContext = selectionContext;
    }

    public void attachHandlers() {
        gamePanel.getCombatMainPanel().getCloseButton().addActionListener(e -> {

            gamePanel.getCombatMainPanel().close();
            gamePanel.setAnyPanelOpen(false);

        });
    }

}
