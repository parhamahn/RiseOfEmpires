package model.item;

import model.unit.Unit;

public class CombatItem extends Item{


    public CombatItem(Unit unit) {
        super(unit);
    }

    @Override
    public void effect() {
        return;
    }

    @Override
    public String isAvailable() {
        if(super.getUnit().isUsedItem()){
            return "Unit already used an item";
        }else{
            return null;
        }
    }
}
