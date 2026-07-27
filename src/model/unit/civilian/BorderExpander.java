package model.unit.civilian;

import model.unit.Unit;

public class BorderExpander extends Unit {

    public BorderExpander(){
        super(UNIT_TYPE.BORDER_EXPANDER, 10, 1, 10);
    }

    @Override
    public boolean isBorderExpander() { return true; }

    @Override
    public boolean isCombatUnit(){
        return false;
    }

}
