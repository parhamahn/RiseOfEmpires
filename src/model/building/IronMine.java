package model.building;

import model.Cost;
import model.Hex;
import model.Task;

public class IronMine extends Building{
    public final static int productingPerUnit = 1;

    private final static Cost cost = new Cost(40, 0,  0, 3);
    private final static Cost upkeep = new Cost(4, 2, 0, 0);


    public IronMine(Hex hex) {
        super(hex, cost, upkeep, 3);
    }


    public static Cost getBuildCost() { return cost; }

    @Override
    public int getProductionPerWorker() { return productingPerUnit; }

    @Override
    public Hex.ResourceType getResourceType() { return Hex.ResourceType.IRON; }

    @Override
    public String getBuildingImageName() { return "ironminehex"; }
}
