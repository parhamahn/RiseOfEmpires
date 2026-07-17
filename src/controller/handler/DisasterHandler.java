package controller.handler;

import model.disasters.Earthquake;
import model.disasters.Flood;
import model.disasters.NaturalDisaster;
import model.disasters.Tsunami;
import model.map.Hex;
import model.state.GameState;
import view.game.GamePanel;

import java.util.ArrayList;
import java.util.List;

public class DisasterHandler {

    GameState gameState;
    GamePanel gamePanel;

    public DisasterHandler(GameState gameState, GamePanel gamePanel) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
    }

    public void handle() {
        List<NaturalDisaster.DisasterType> disasters = NaturalDisaster.getRandomDisasters();
        for (NaturalDisaster.DisasterType disaster : disasters) {
            switch (disaster) {
                case EARTHQUAKE -> {
                    Earthquake eq = new Earthquake(gameState.getWorldState().getMap());
                    List<Hex> cracks = new ArrayList<>();
                    boolean anyDiscovered = false;
                    for (Hex involved : eq.getInvolvedHexes()) {
                        if (involved.isDiscovered()) {
                            cracks.add(involved);
                            anyDiscovered = true;
                        }
                    }
                    if (anyDiscovered) {
                        gamePanel.getMapPanel().startShake();
                    }
                    gamePanel.getMapPanel().showCracks(cracks);
                    eq.effect(gameState);
                }
                case FLOOD -> {
                    Flood flood = new Flood(gameState.getWorldState().getMap());
                    flood.effect(gameState);
                }
                case TSUNAMI -> {
                    Tsunami ts = new Tsunami(gameState.getWorldState().getMap());
                    ts.effect(gameState);
                }
            }
        }
    }

}