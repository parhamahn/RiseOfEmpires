package model.building.empire;

import model.building.ProducingBuilding;
import model.storage.Cost;
import model.map.Hex;

import java.io.Serializable;

public class Stable extends ProducingBuilding implements Serializable {
private static final long serialVersionUID = 1L;
    public final static int productingPerUnit = 3;
    Hex.ResourceType breeding;

    private final static Cost cost = new Cost(20, 0,  0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0, 0);


    public Stable(Hex hex, Hex.ResourceType breeding) {super(hex, cost, upkeep, 2, 50); this.breeding = breeding;}


    public Hex.ResourceType getBreeding() {
        return breeding;
    }
    public static Cost getBuildCost() { return cost; }


    @Override
    public int getProductionPerWorker() { return productingPerUnit; }

    @Override
    public Hex.ResourceType getResourceType() { return breeding; }
}
