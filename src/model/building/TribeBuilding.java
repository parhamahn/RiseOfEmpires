package model.building;

import model.map.Hex;

public abstract class TribeBuilding extends Building {
    private final String imageName;

    public TribeBuilding(Hex hex, String imageName) {
        super(hex, null, null, 0, 200);
        this.imageName = imageName;
    }

    @Override
    public int getProductionPerWorker() {
        return 0;
    }

    @Override
    public String getBuildingImageName() {
        return imageName;
    }
}
