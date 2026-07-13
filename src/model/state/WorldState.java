package model.state;

import model.Season;
import model.map.Map;

public class WorldState {
    private Map map;
    private int turnNumber;
    private Season.SeasonType season;

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
        this.season = Season.SeasonType.SPRING;
    }
}
