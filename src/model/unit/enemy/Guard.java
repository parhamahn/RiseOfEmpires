package model.unit.enemy;

import model.building.tribes.Tribe;
import model.unit.Unit;

import java.io.Serializable;

public class Guard extends Unit implements Serializable {
private static final long serialVersionUID = 1L;

    private final Tribe tribe;

    public Guard(Tribe tribe) {
        super(UNIT_TYPE.GUARD, 2, 0, 40);
        this.tribe = tribe;
    }

    @Override
    public boolean isCombatUnit() {
        return true;
    }

    @Override
    public String getUnitImageName() {
        return "guard";
    }

    public Tribe getTribe() {
        return tribe;
    }
}
