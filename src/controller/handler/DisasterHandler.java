package controller.handler;

import helpers.sound.SoundEffects;
import model.Season;
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
        List<NaturalDisaster.DisasterType> disasters = NaturalDisaster.getRandomDisasters(gameState.getRng());
        if(gameState.getEmpireState().getSeason()!= Season.SeasonType.FALL && disasters.contains(NaturalDisaster.DisasterType.FLOOD)){
            disasters.remove(NaturalDisaster.DisasterType.FLOOD);
        }
        for (NaturalDisaster.DisasterType disaster : disasters) {
            switch (disaster) {
                case EARTHQUAKE -> {
                    Earthquake eq = new Earthquake(gameState.getWorldState().getMap(), gameState.getRng());
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
                    SoundEffects.getInstance().playEarthquakeSoundEffect();
                    eq.effect(gameState);
                }
                case FLOOD -> {
                    Flood flood = new Flood(gameState.getWorldState().getMap(), gameState.getRng());
                    List<Hex> visibleHexes = new ArrayList<>();
                    for (Hex involved : flood.getInvolvedHexes()) {
                        if (involved.isDiscovered()) {
                            visibleHexes.add(involved);
                        }
                    }
                    if (!visibleHexes.isEmpty()) {
                        gamePanel.getMapPanel().showFlood(flood.getCenter(), visibleHexes);
                    }
                    SoundEffects.getInstance().playFloodSoundEffect();
                    flood.effect(gameState);
                }
                case TSUNAMI -> {
                    Tsunami ts = new Tsunami(gameState.getWorldState().getMap(), gameState.getRng());
                    List<Hex> visibleHexes = new ArrayList<>();
                    for (Hex involved : ts.getInvolvedHexes()) {
                        if (involved.isDiscovered()) {
                            visibleHexes.add(involved);
                        }
                    }
                    if (!visibleHexes.isEmpty()) {
                        gamePanel.getMapPanel().showTsunami(ts.getCenter(), visibleHexes);
                    }
                    SoundEffects.getInstance().playFloodSoundEffect();
                    ts.effect(gameState);
                }
            }
        }
    }

}