package model.state;

import model.Season;
import model.map.Map;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;

public class WorldState {
    private Map map;
    private int turnNumber;
    private List<Unit> enemies = new ArrayList<>();

    public WorldState(Map map) {
        reset(map);
    }

    public Map getMap() { return map; }
    public int getTurnNumber() { return turnNumber; }
    public void nextTurn() { this.turnNumber++; }

    public void reset(Map map){
        this.turnNumber = 1;
        this.map = map;
        this.enemies.clear();
    }

    public List<Unit> getEnemies() {
        return enemies;
    }
}
