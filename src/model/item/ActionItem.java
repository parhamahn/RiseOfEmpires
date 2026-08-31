package model.item;

import model.storage.Storage;
import model.unit.Unit;

public class ActionItem extends Item{


    public ActionItem(Unit unit) {
        super(unit);
    }

    @Override
    public void effect(Storage storage) {
        if(isAvailable(storage)==null){
            super.getUnit().setActionPoints(super.getUnit().getActionPoints()+2);
            getUnit().setUsedItem(true);
            storage.consumeActionItem();
        }
    }

    @Override
    public String isAvailable(Storage storage) {
        if(super.getUnit().isUsedItem()){
            return "Unit already used an item";
        }else if(storage.getActionItemCount()==0){
            return "You don't have any action item";
        }else{
            return null;
        }
    }
}
