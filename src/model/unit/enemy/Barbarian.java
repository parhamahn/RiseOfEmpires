package model.unit.enemy;

import model.unit.Unit;

import java.io.Serializable;

public class Barbarian extends Unit implements Serializable {
private static final long serialVersionUID = 1L;

    public Barbarian(){
        super(UNIT_TYPE.BARBARIAN, 10, 0, 40);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
