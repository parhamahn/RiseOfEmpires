package model.unit.combat;

import model.unit.Unit;

public class Archer extends Unit {

    public Archer(){
        super(UNIT_TYPE.ARCHER, 10, 1, 20);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
