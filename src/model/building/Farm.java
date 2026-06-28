package model.building;

import model.Cost;
import model.Hex;

public class Farm extends Building{

    private final static Cost cost = new Cost(5, 0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0);

    public Farm(Hex hex) {
        super(hex, cost, upkeep, 2);
    }

}
