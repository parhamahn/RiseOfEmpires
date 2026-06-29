package model.building;

import model.Cost;
import model.Hex;
import model.Task;

public class Town extends Building{

    private final static Cost cost = Task.getCostForTask(Task.TASK.TOWN);
    private final static Cost upkeep = new Cost(5, 3, 0, 0);

    public Town(Hex hex) {
        super(hex, cost, upkeep, 0);
    }
}
