package model.unit;

public class UnitFactory {

    public static Unit createUnit(Unit.UNIT_TYPE unitType) {
        return switch (unitType) {
            case WORKER -> new Worker();
            case BORDER_EXPANDER -> new BorderExpander();
            case BUILDER -> new Builder();
            case EXPLORER -> new Explorer();
            case ARCHER -> new Archer();
            case SWORDSMAN -> new Swordsman();
            case CAVALRY -> new Cavalry();
            case CATAPULT -> new Catapult();
        };
    }
}
