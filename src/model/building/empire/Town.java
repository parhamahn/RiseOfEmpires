package model.building.empire;

import model.building.Building;
import model.storage.Cost;
import model.map.Hex;

import java.io.Serializable;

public class Town extends Building implements Serializable {
private static final long serialVersionUID = 1L;

    private final static Cost cost = new Cost(40, 20, 5, 0, 3);
    private final static Cost upkeep = new Cost(3, 2, 0, 0, 0);


    public Town(Hex hex) {
        super(hex, cost, upkeep, 100);
    }


    public static Cost getBuildCost() { return cost; }

    @Override
    public boolean isTown() { return true; }
}
