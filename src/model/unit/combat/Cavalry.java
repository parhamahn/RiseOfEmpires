package model.unit.combat;

import model.unit.Unit;

public class Cavalry extends Unit {

    public Cavalry(){
        super(UNIT_TYPE.CAVALRY, 15, 1, 35);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
