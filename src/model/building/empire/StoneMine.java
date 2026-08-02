package model.building.empire;

import model.building.Building;
import model.storage.Cost;
import model.map.Hex;

import java.io.Serializable;

public class StoneMine extends Building implements Serializable {
private static final long serialVersionUID = 1L;
    public final static int productingPerUnit = 2;

    private final static Cost cost = new Cost(25, 0,  0, 0, 3);
    private final static Cost upkeep = new Cost(3, 2, 0, 0, 0);


    public StoneMine(Hex hex) {
        super(hex, cost, upkeep, 3, 75);
    }


    public static Cost getBuildCost() { return cost; }

    @Override
    public int getProductionPerWorker() { return productingPerUnit; }

    @Override
    public Hex.ResourceType getResourceType() { return Hex.ResourceType.STONE; }

    @Override
    public String getBuildingImageName() { return "stoneminehex"; }
}
