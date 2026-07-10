package model.unit;

public class Swordsman extends Unit{

    public Swordsman(){
        super(UNIT_TYPE.SWORDSMAN, 10, 1, 40);
    }

    @Override
    public boolean isCombatUnit(){
        return true;
    }

}
