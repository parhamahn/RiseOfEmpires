package model.building.empire;

import model.building.Building;
import model.storage.Cost;
import model.map.Hex;

public class Stable extends Building {
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

    @Override
    public String getBuildingImageName() { return "stablehex"; }
}
