package model.disasters;

import model.map.Hex;
import model.state.GameState;

import java.util.List;

public interface Disaster {
    Hex getCenter();
    List<Hex> getInvolvedHexes();
    void effect(GameState gameState);
}
