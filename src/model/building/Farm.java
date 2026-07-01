package model.building;

import model.Cost;
import model.Hex;
import model.Task;

public class Farm extends Building{
    public final static int productingPerUnit = 2;

    private Hex.ResourceType farming;
    private final static Cost cost = Task.getCostForTask(Task.TASK.FARM);
    private final static Cost upkeep = new Cost(2, 1, 0, 0);

    public Farm(Hex hex) {
        super(hex, cost, upkeep, 2);
    }

    public Hex.ResourceType getFarming() {
        return farming;
    }

    public void setFarming(Hex.ResourceType farming) {
        this.farming = farming;
    }
}
