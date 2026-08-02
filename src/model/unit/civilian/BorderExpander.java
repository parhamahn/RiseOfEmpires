package model.unit.civilian;

import model.unit.Unit;

import java.io.Serializable;

public class BorderExpander extends Unit  implements Serializable {
private static final long serialVersionUID = 1L;

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
