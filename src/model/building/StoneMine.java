package model.building;

import model.Cost;
import model.Hex;
import model.Task;

public class StoneMine extends Building{

    private final static Cost cost = Task.getCostForTask(Task.TASK.STONE_MINE);
    private final static Cost upkeep = new Cost(3, 2, 0, 0);

    public StoneMine(Hex hex) {
        super(hex, cost, upkeep, 3);
    }

}
