package model.building;

import model.Hex;

public class TownHall extends Building {

    public TownHall(Hex hex){super(hex, null, null, 0);}


    @Override
    public int getProductionPerWorker() { return 0; }

    @Override
    public String getBuildingImageName() { return "townhall"; }

    @Override
    public boolean isTownHall() { return true; }
}