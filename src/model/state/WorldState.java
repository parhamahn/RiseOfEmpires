package model.state;

import model.Season;
import model.building.other.TradingCenter;
import model.building.tribes.Tribe;
import model.map.Map;
import model.unit.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorldState implements Serializable {
private static final long serialVersionUID = 1L;
    private Map map;
    private int turnNumber;
    private List<Unit> enemies = new ArrayList<>();
    private List<Tribe>  tribes = new ArrayList<>();
    private List<TradingCenter> tradingCenters = new ArrayList<>();

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
        this.tribes.clear();
        this.tradingCenters.clear();
    }

    public List<Unit> getEnemies() {
        return enemies;
    }
    public List<Tribe> getTribes() { return tribes; }
    public List<TradingCenter> getTradingCenters() { return tradingCenters; }
}
