package model.item;

import model.storage.Storage;
import model.unit.Unit;

public abstract class Item {

    private Unit unit;

    public Item(Unit unit){
        this.unit = unit;
    }

    public abstract void effect(Storage storage);
    public abstract String isAvailable(Storage storage);

    public Unit getUnit() {
        return unit;
    }
}
