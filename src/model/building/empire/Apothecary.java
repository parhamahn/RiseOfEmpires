package model.building.empire;

import model.TownHallLevel;
import model.building.Building;
import model.map.Hex;
import model.storage.Cost;

import java.io.Serializable;

public class Apothecary extends Building implements Serializable {

    private static final Cost cost = new Cost(20, 5, 0, 0, 0);
    private final static Cost upkeep = new Cost(3, 2, 0, 0, 0);

    public Apothecary(Hex hex) {
        super(hex, cost, upkeep, 100);
    }

    public static TownHallLevel getLevel() { return TownHallLevel.SETTLEMENT; }

    public static Cost getBuildCost() { return cost; }
    public boolean isApothecary(){return true;}
}
