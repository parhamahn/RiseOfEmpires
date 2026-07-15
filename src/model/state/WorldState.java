package model.state;

import model.Season;
import model.map.Map;

public class WorldState {
    private Map map;
    private int turnNumber;

    public WorldState(Map map) {
        reset(map);
    }

    public Map getMap() { return map; }
    public int getTurnNumber() { return turnNumber; }
    public void nextTurn() { this.turnNumber++; }

    public void reset(Map map){
        this.turnNumber = 1;
        this.map = map;
    }
}
