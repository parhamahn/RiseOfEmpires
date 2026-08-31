package model.item;

import model.unit.Unit;

public class ActionItem extends Item{


    public ActionItem(Unit unit) {
        super(unit);
    }

    @Override
    public void effect() {
        if(isAvailable()==null){
            super.getUnit().setActionPoints(super.getUnit().getActionPoints()+2);
        }
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
