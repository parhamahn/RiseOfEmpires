package model.building;

import model.Cost;
import model.Hex;

public class StoneMine extends Building{

    private final static Cost cost = new Cost(50, 25, 0, 3);
    private final static Cost upkeep = new Cost(3, 2, 0, 0);

    public StoneMine(Hex hex) {
        super(hex, cost, upkeep, 3);
    }

}
