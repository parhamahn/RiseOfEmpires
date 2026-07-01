package model.building;

import model.Cost;
import model.Hex;
import model.Task;

public class LumberMill extends Building{
    public final static int productingPerUnit = 3;

    private final static Cost cost = Task.getCostForTask(Task.TASK.LUMBER_MILL);
    private final static Cost upkeep = new Cost(2, 1, 0, 0);

    public LumberMill(Hex hex) {
        super(hex, cost, upkeep, 2);
    }

}
