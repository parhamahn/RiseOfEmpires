package model.building;

import model.map.Hex;

public class TraderTribe extends TribeBuilding {
    public TraderTribe(Hex hex) {
        super(hex, "tradertribe");
    }

    public static double getMultiplier() {
        return 0.8;
    }
}
