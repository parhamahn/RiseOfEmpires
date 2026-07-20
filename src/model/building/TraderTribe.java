package model.building;

import model.map.Hex;

public class TraderTribe extends Building{

    public TraderTribe(Hex hex) {
        super(hex, null, null, 0, 200);
    }


    @Override
    public int getProductionPerWorker() {
        return 0;
    }

    @Override
    public String getBuildingImageName() {
        return "tradertribe";
    }

    public static double getMultiplier() {return 0.8;}

}
