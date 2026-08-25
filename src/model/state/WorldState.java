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
    private Season.SeasonType season = Season.SeasonType.SPRING;
    private List<Unit> enemies = new ArrayList<>();
    private List<Tribe>  tribes = new ArrayList<>();
    private List<TradingCenter> tradingCenters = new ArrayList<>();

    public WorldState(Map map) {
        reset(map);
    }

    public Map getMap() { return map; }
    public int getTurnNumber() { return turnNumber; }
    public void nextTurn() { this.turnNumber++; }
    public Season.SeasonType getSeason() { return season; }
    public void setSeason(Season.SeasonType season) { this.season = season; }

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

    public void loadFrom(WorldState loaded){
        this.map = loaded.getMap();
        this.turnNumber = loaded.getTurnNumber();
        this.season = loaded.getSeason();
        this.enemies = loaded.getEnemies();
        this.tribes = loaded.getTribes();
        this.tradingCenters = loaded.getTradingCenters();
    }
}
