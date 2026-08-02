package model.unit.civilian;

import model.unit.Unit;

import java.io.Serializable;

public class Explorer extends Unit  implements Serializable {
private static final long serialVersionUID = 1L;

    public Explorer(){
        super(UNIT_TYPE.EXPLORER, 5, 1, 10);
    }

    @Override
    public boolean isExplorer() { return true; }

    @Override
    public boolean isCombatUnit(){
        return false;
    }

}
