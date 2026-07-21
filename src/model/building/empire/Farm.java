package model.building.empire;

import model.building.Building;
import model.storage.Cost;
import model.map.Hex;

public class Farm extends Building {
    public final static int productingPerUnit = 2;

    private Hex.ResourceType farming;
    private final static Cost cost = new Cost(5,  0,  0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0, 0);

    public Farm(Hex hex, Hex.ResourceType farming) {super(hex, cost, upkeep, 2, 50);this.farming = farming;}


    public Hex.ResourceType getFarming() {return farming;}
    public static Cost getBuildCost() { return cost; }


    @Override
    public int getProductionPerWorker() { return productingPerUnit; }

    @Override
    public Hex.ResourceType getResourceType() { return farming; }

    @Override
    public String getBuildingImageName() { return "farmhex"; }
}
