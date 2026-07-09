package model.building;

import model.storage.Cost;
import model.map.Hex;

public class LumberMill extends Building{
    public final static int productingPerUnit = 3;

    private final static Cost cost = new Cost(5,  0,  0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0, 0);


    public LumberMill(Hex hex) {
        super(hex, cost, upkeep, 2, 50);
    }


    public static Cost getBuildCost() { return cost; }


    @Override
    public int getProductionPerWorker() { return productingPerUnit; }

    @Override
    public Hex.ResourceType getResourceType() { return Hex.ResourceType.WOOD; }

    @Override
    public String getBuildingImageName() { return "lumbermillhex"; }


}
