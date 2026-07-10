package model.building;

import model.map.Hex;
import model.storage.Cost;

public class Bazaar extends Building{

    private final static Cost cost = new Cost(20, 10,  0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0, 0);

    public Bazaar(Hex hex){
        super(hex, cost, upkeep, 0, 50);
    }

    public static Cost getBuildCost() { return cost; }

    @Override
    public int getProductionPerWorker() {
        return 0;
    }

    @Override
    public String getBuildingImageName() {
        return "bazaarhex";
    }
}
