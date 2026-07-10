package model.unit;

public class Cavalry extends Unit{

    public Cavalry(){
        super(UNIT_TYPE.CAVALRY, 10, 1, 35);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
