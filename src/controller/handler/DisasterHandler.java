package controller.handler;

import helpers.sound.SoundEffects;
import model.disasters.BearAttack;
import model.disasters.Earthquake;
import model.disasters.Flood;
import model.disasters.Disaster;
import model.disasters.Tsunami;
import model.map.Hex;
import model.state.GameState;
import view.common.WarningDialogs;
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

    public void animate(List<Disaster> disasters) {
        for (Disaster disaster : disasters) {
            if (disaster instanceof Earthquake earthquake) {
                animateEarthquake(earthquake);
            } else if (disaster instanceof Flood flood) {
                animateFlood(flood);
            } else if (disaster instanceof Tsunami tsunami) {
                animateTsunami(tsunami);
            } else if (disaster instanceof BearAttack bearAttack) {
                animateBearAttack(bearAttack);
            }
        }
    }

    private void animateEarthquake(Earthquake eq) {
        if (eq.getCenter() == null) return;
        java.util.UUID playerId = gameState.getLocalPlayerId();
        List<Hex> cracks = new ArrayList<>();
        boolean anyDiscovered = false;
        for (Hex involved : eq.getInvolvedHexes()) {
            if (involved.isDiscoveredBy(playerId)) {
                cracks.add(involved);
                anyDiscovered = true;
            }
        }
        if (anyDiscovered) {
            gamePanel.getMapPanel().startShake();
            SoundEffects.getInstance().playEarthquakeSoundEffect();
        }
        gamePanel.getMapPanel().showCracks(cracks);
    }

    private void animateFlood(Flood flood) {
        if (flood.getCenter() == null) return;
        java.util.UUID playerId = gameState.getLocalPlayerId();
        List<Hex> visibleHexes = new ArrayList<>();
        for (Hex involved : flood.getInvolvedHexes()) {
            if (involved.isDiscoveredBy(playerId)) {
                visibleHexes.add(involved);
            }
        }
        if (!visibleHexes.isEmpty()) {
            gamePanel.getMapPanel().showFlood(flood.getCenter(), visibleHexes);
            SoundEffects.getInstance().playFloodSoundEffect();
        }
    }

    private void animateTsunami(Tsunami tsunami) {
        if (tsunami.getCenter() == null) return;
        java.util.UUID playerId = gameState.getLocalPlayerId();
        List<Hex> visibleHexes = new ArrayList<>();
        for (Hex involved : tsunami.getInvolvedHexes()) {
            if (involved.isDiscoveredBy(playerId)) {
                visibleHexes.add(involved);
            }
        }
        if (!visibleHexes.isEmpty()) {
            gamePanel.getMapPanel().showTsunami(tsunami.getCenter(), visibleHexes);
            SoundEffects.getInstance().playFloodSoundEffect();
        }
    }

    private void animateBearAttack(BearAttack bearAttack) {
        if (bearAttack.getCenter() == null) return;
        java.util.UUID playerId = gameState.getLocalPlayerId();
        boolean anyDiscovered = bearAttack.getInvolvedHexes().stream().anyMatch(h -> h.isDiscoveredBy(playerId));
        if (anyDiscovered) {
            WarningDialogs.bearAttack();
        }
    }
}
