package model.building;

import model.map.Hex;

public class MountainTribe extends Building{

    public MountainTribe(Hex hex) {
        super(hex, null, null, 0, 200);
    }

    @Override
    public int getProductionPerWorker() {
        return 0;
    }

    @Override
    public String getBuildingImageName() {
        return "mountaintribe";
    }


}
