package model.building.empire;

import model.building.Building;
import model.map.Hex;

public class TownHall extends Building {

    private int HP;

    public TownHall(Hex hex){
        super(hex, null, null, 0, 200);
        this.HP = 200;
    }


    @Override
    public int getProductionPerWorker() { return 0; }

    @Override
    public String getBuildingImageName() { return "townhall"; }

    @Override
    public boolean isTownHall() { return true; }

    public int getHP(){return HP;}
    public void setHP(int HP){this.HP = HP;}
}