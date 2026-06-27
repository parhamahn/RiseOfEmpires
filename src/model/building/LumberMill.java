package model.building;

import model.Cost;
import model.Hex;

public class LumberMill extends Building{

    private final static Cost cost = new Cost(10, 5, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0);

    public LumberMill(Hex hex) {
        super(hex, cost, upkeep, 2);
    }

}
