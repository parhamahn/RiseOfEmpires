package model.building.empire;

import model.TownHallLevel;
import model.building.Building;
import model.map.Hex;
import model.storage.Cost;

import java.io.Serializable;

public class Dock extends Building implements Serializable {
private static final long serialVersionUID = 1L;

    private static Cost cost = new Cost(20, 0,  0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0, 0);

    public Dock(Hex hex) {
        super(hex, cost, upkeep, 2, 75);
    }

    public static TownHallLevel getLevel() { return TownHallLevel.SETTLEMENT; }

    public static Cost getBuildCost() { return cost; }

    @Override
    public int getProductionPerWorker() {
        return 2;
    }

    @Override
    public String getBuildingImageName() {
        return "dockhex";
    }
}
