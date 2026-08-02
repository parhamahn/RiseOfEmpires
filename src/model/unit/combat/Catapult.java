package model.unit.combat;

import model.unit.Unit;

import java.io.Serializable;

public class Catapult extends Unit implements Serializable {
private static final long serialVersionUID = 1L;

    public Catapult(){
        super(UNIT_TYPE.CATAPULT, 10, 1, 50);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
