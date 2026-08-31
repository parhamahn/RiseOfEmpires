package model.item;

import model.storage.Storage;
import model.unit.Unit;

public class CombatItem extends Item{


    public CombatItem(Unit unit) {
        super(unit);
    }

    @Override
    public void effect(Storage storage) {
        return;
    }

    @Override
    public String isAvailable(Storage storage) {
        if(super.getUnit().isUsedItem()){
            return "Unit already used an item";
        }else if(storage.getCombatItemCount()==0){
            return "You don't have any combat item";
        }else{
            return null;
        }
    }
}
