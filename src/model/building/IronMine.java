package model.building;

import model.Cost;
import model.Hex;

public class IronMine extends Building{

    private final static Cost cost = new Cost(40, 0, 0, 3);
    private final static Cost upkeep = new Cost(4, 2, 0, 0);


    public IronMine(Hex hex) {
        super(hex, cost, upkeep, 3);
    }
}
