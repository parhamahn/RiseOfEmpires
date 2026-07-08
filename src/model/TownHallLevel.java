package model;

import model.storage.Cost;
import model.storage.MaxStorage;

public enum TownHallLevel {

    BASE_CAMP(new MaxStorage(100, 100, 100, 100), 0, null),
    SETTLEMENT(new MaxStorage(200, 200, 200, 150), 3, new Cost(50, 50, 0, 0)),
    CAPITAL(new MaxStorage(500, 500, 500, 300), 5, new Cost(0, 100, 50, 0));

    private final MaxStorage maxStorage;
    private final int nextUpgradeTurnCost;
    private final Cost cost;


    TownHallLevel(MaxStorage maxStorage, int nextUpgradeTurnCost, Cost cost) {
        this.maxStorage = maxStorage;
        this.nextUpgradeTurnCost = nextUpgradeTurnCost;
        this.cost = cost;
    }


    public MaxStorage getMaxStorage() {
        return maxStorage;
    }
    public int getNextUpgradeTurnCost() {
        return nextUpgradeTurnCost;
    }
    public Cost getCost() {return cost;}


    public TownHallLevel nextUpgrade() {
        TownHallLevel[] values = values();
        int nextOrdinal = ordinal() + 1;
        return nextOrdinal < values.length ? values[nextOrdinal] : null;
    }
}