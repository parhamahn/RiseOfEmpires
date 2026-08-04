package model.unit.combat;

import model.unit.Unit;

import java.io.Serializable;

public class Swordsman extends Unit  implements Serializable {
private static final long serialVersionUID = 1L;

    public Swordsman(){
        super(UNIT_TYPE.SWORDSMAN, 2, 1, 40);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
