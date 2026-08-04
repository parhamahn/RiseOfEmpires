package model.unit.combat;

import model.unit.Unit;

import java.io.Serializable;

public class Cavalry extends Unit  implements Serializable {
private static final long serialVersionUID = 1L;

    public Cavalry(){
        super(UNIT_TYPE.CAVALRY, 4, 1, 35);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
