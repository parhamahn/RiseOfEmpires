package model.building;

import model.storage.Cost;
import model.map.Hex;

public class Town extends Building{

    private final static Cost cost = new Cost(40, 20, 5, 3);
    private final static Cost upkeep = new Cost(3, 2, 0, 0);


    public Town(Hex hex) {
        super(hex, cost, upkeep, 0);
    }


    public static Cost getBuildCost() { return cost; }

    @Override
    public int getProductionPerWorker() { return 0; }

    @Override
    public String getBuildingImageName() { return "townhex"; }

    @Override
    public boolean isTown() { return true; }
}
