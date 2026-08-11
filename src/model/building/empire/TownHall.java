package model.building.empire;

import model.building.Building;
import model.map.Hex;

import java.io.Serializable;

public class TownHall extends Building implements Serializable {
private static final long serialVersionUID = 1L;

    public TownHall(Hex hex){
        super(hex, null, null, 200);
    }

    @Override
    public boolean isTownHall() { return true; }
}
