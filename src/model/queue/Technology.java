package model.queue;

import model.TownHallLevel;
import model.storage.Cost;

public enum Technology {

    SAILING(4, new Cost(80,0,0,0, 0), TownHallLevel.SETTLEMENT),
    STEEL_TOOLS(3, new Cost(0,0,40,0, 0), TownHallLevel.SETTLEMENT),
    DEFENSIVE_PATTERN(4, new Cost(0,100,0,0, 0), TownHallLevel.CAPITAL);

    private final int turnCost;
    private final Cost cost;
    private final TownHallLevel townHallLevel;


    Technology(int turnCost, Cost cost, TownHallLevel townHallLevel) {
        this.cost = cost;
        this.turnCost = turnCost;
        this.townHallLevel = townHallLevel;
    }


    public int getTurnCost() {
        return turnCost;
    }
    public Cost getCost() {return cost;}
    public TownHallLevel getTownHallLevel() {return townHallLevel;}
}
