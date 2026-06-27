package model.building;

import model.Cost;
import model.Hex;

public class Town extends Building{

    private final static Cost cost = new Cost(50, 25, 0, 3);
    private final static Cost upkeep = new Cost(5, 3, 0, 0);

    public Town(Hex hex) {
        super(hex, cost, upkeep, 0);
    }
}
