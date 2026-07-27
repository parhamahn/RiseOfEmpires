package model.unit.civilian;

import model.unit.Unit;

public class Worker extends Unit {

    public Worker(){super(UNIT_TYPE.WORKER, 5, 2, 10);}

    @Override
    public boolean isWorker() { return true; }

    @Override
    public boolean isCombatUnit(){
        return false;
    }

}
