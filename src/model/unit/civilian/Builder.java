package model.unit.civilian;

import model.unit.Unit;

import java.io.Serializable;

public class Builder extends Unit implements Serializable {
private static final long serialVersionUID = 1L;

    private int charges;

    public Builder(){
        super(UNIT_TYPE.BUILDER, 5, 2, 10);
        this.charges=3;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    @Override
    public boolean isBuilder() { return true; }

    @Override
    public boolean isCombatUnit(){
        return false;
    }

}
