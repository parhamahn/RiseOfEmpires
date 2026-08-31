package model.item;

import model.unit.Unit;

public abstract class Item {

    private Unit unit;

    public Item(Unit unit){
        this.unit = unit;
    }

    public abstract void effect();
    public abstract String isAvailable();

    public Unit getUnit() {
        return unit;
    }
}
