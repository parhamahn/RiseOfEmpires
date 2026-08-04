package model.unit.civilian;

import model.unit.Unit;

import java.io.Serializable;

public class Worker extends Unit  implements Serializable {
private static final long serialVersionUID = 1L;

    public Worker(){super(UNIT_TYPE.WORKER, 5, 1, 10);}

    @Override
    public boolean isWorker() { return true; }

    @Override
    public boolean isCombatUnit(){
        return false;
    }

}
