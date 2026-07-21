package model.building.tribes;

import model.map.Hex;

public class TraderTribe extends Tribe {
    public TraderTribe(Hex hex) {
        super(hex, "tradertribe");
    }

    public static double getMultiplier() {
        return 0.8;
    }
}
