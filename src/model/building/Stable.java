package model.building;

import model.Cost;
import model.Hex;
import model.Task;

public class Stable extends Building {
    public final static int productingPerUnit = 3;
    Hex.ResourceType breeding;

    private final static Cost cost = Task.getCostForTask(Task.TASK.STABLE);
    private final static Cost upkeep = new Cost(2, 1, 0, 0);

    public Stable(Hex hex, Hex.ResourceType breeding) {
        super(hex, cost, upkeep, 2);
        this.breeding = breeding;
    }

    public Hex.ResourceType getBreeding() {
        return breeding;
    }

    @Override
    public int getProductionPerWorker() { return productingPerUnit; }

    @Override
    public Hex.ResourceType getResourceType() { return breeding; }

    @Override
    public String getBuildingImageName() { return "stablehex"; }
}
