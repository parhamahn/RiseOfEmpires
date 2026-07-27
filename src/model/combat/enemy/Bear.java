package model.combat.enemy;

import model.unit.Unit;

public class Bear extends Unit {

    public Bear(){
        super(UNIT_TYPE.BEAR, 2, 0, 120);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
