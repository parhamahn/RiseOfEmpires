package model.unit;

public class Catapult extends Unit {

    public Catapult(){
        super(UNIT_TYPE.CATAPULT, 10, 1, 50);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
