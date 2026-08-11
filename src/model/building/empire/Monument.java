package model.building.empire;

import model.building.Building;
import model.map.Hex;
import model.storage.Cost;

import java.io.Serializable;

public class Monument extends Building implements Serializable {
private static final long serialVersionUID = 1L;

    private final static Cost cost = new Cost(20, 10,  0, 0, 2);
    private final static Cost upkeep = new Cost(2, 1, 0, 0, 0);

    public Monument(Hex hex) {
        super(hex, cost, upkeep, 50);
    }

    public static Cost getBuildCost() { return cost; }

}
