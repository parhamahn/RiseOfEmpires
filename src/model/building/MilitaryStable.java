package model.building;

import model.TownHallLevel;
import model.map.Hex;
import model.storage.Cost;

public class MilitaryStable extends Building {

    private final static Cost cost = new Cost(20, 0,  0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0, 0);

    public MilitaryStable(Hex hex) {
        super(hex, cost, upkeep, 0, 100);
    }

    public static TownHallLevel getLevel() { return TownHallLevel.SETTLEMENT; }

    public static Cost getBuildCost() { return cost; }

    @Override
    public int getProductionPerWorker() {
        return 0;
    }

    @Override
    public String getBuildingImageName() {
        return "militarystablehex";
    }
}
