package model.queue;

import model.unit.Unit;

public class CookQueue extends Queue {

    private Unit unit;


    public CookQueue(Unit unit, int turnCost, int startTurn){
        super(QUEUE_TYPE.COOK, turnCost, startTurn);
        this.unit = unit;
    }


    public Unit getUnit() {
        return unit;
    }
}
