package model;

import model.unit.Unit;

public class CookQueue {

    private Unit unit;
    private int turnToCook;

    public CookQueue(Unit unit, int turnToCook){
        this.unit = unit;
        this.turnToCook = turnToCook;
    }

    public int getTurnToCook() {
        return turnToCook;
    }

    public void setTurnToCook(int turnToCook) {
        this.turnToCook = turnToCook;
    }

    public Unit getUnit() {
        return unit;
    }
}
