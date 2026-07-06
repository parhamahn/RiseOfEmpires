package model.state;

import model.Map;

public class WorldState {
    private Map map;
    private int turnNumber;

    public WorldState(Map map) {
        this.map = map;
        this.turnNumber = 1;
    }

    public Map getMap() { return map; }
    public int getTurnNumber() { return turnNumber; }
    public void nextTurn() { this.turnNumber++; }
}
