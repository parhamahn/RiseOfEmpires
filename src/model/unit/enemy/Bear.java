package model.unit.enemy;

import model.unit.Unit;

import java.io.Serializable;

public class Bear extends Unit implements Serializable {
private static final long serialVersionUID = 1L;

    public Bear(){
        super(UNIT_TYPE.BEAR, 2, 0, 120);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
