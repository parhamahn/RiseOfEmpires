package model.building.empire;

import model.TownHallLevel;
import model.building.TradingBuilding;
import model.map.Hex;
import model.storage.Cost;

import java.io.Serializable;

public class Bazaar extends TradingBuilding implements Serializable {
private static final long serialVersionUID = 1L;
    private final static Cost cost = new Cost(20, 10, 0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0, 0);

    public Bazaar(Hex hex) {
        super(hex, cost, upkeep, 50);
    }

    public static Cost getBuildCost() { return cost; }

    public static TownHallLevel getLevel() { return TownHallLevel.SETTLEMENT; }

    @Override
    public double getMultiplier(int quantity) {
        return switch (quantity) {
            case 10  -> 0.5;
            case 100 -> 0.6;
            case 500 -> 0.7;
            default  -> 0.5;
        };
    }
}
