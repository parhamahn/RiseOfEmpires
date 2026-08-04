package model.unit.combat;

import model.unit.Unit;

import java.io.Serializable;

public class Archer extends Unit  implements Serializable {
private static final long serialVersionUID = 1L;

    public Archer(){
        super(UNIT_TYPE.ARCHER, 2, 1, 20);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
