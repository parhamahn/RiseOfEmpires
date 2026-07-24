package controller.handler.building.tribe;

import model.building.tribes.Tribe;
import model.state.GameState;
import view.game.GamePanel;

public class TribeHandler {

    private Tribe tribe;
    private GameState gameState;
    private GamePanel gamePanel;

    public TribeHandler(GameState gameState, GamePanel gamePanel){
        this.gameState = gameState;
        this.gamePanel = gamePanel;
    }

    public void attachHandlers(){

        gamePanel.getTribeMainPanel().getCloseButton().addActionListener(e -> {
            gamePanel.getTribeMainPanel().close();
            gamePanel.setAnyPanelOpen(false);
        });

    }

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
        gamePanel.getTribeMainPanel().setTribe(tribe);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
}
