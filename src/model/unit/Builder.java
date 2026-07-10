package model.unit;

public class Builder extends Unit{

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
