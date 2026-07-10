package model.unit;

public class Explorer extends Unit{

    public Explorer(){
        super(UNIT_TYPE.EXPLORER, 5, 1, 10);
    }

    @Override
    public boolean isExplorer() { return true; }

    @Override
    public boolean isCombatUnit(){
        return false;
    }

}
