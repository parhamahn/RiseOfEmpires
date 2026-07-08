package model.building;

import model.storage.Cost;
import model.map.Hex;

public class StoneMine extends Building{
    public final static int productingPerUnit = 2;

    private final static Cost cost = new Cost(25, 0,  0, 3);
    private final static Cost upkeep = new Cost(3, 2, 0, 0);


    public StoneMine(Hex hex) {
        super(hex, cost, upkeep, 3);
    }


    public static Cost getBuildCost() { return cost; }

    @Override
    public int getProductionPerWorker() { return productingPerUnit; }

    @Override
    public Hex.ResourceType getResourceType() { return Hex.ResourceType.STONE; }

    @Override
    public String getBuildingImageName() { return "stoneminehex"; }
}
