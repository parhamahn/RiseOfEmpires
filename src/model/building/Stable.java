package model.building;

import model.Cost;
import model.Hex;
import model.Task;

public class Stable extends Building {

    private final static Cost cost = Task.getCostForTask(Task.TASK.STABLE);
    private final static Cost upkeep = new Cost(2, 1, 0, 0);

    public Stable(Hex hex) {
        super(hex, cost, upkeep, 2);
    }
}
