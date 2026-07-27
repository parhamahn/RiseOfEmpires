package model.combat.enemy;

import model.unit.Unit;

public class Barbarian extends Unit {

    public Barbarian(){
        super(UNIT_TYPE.BARBARIAN, 10, 0, 40);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
