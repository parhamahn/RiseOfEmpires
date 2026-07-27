package model.unit;

import model.unit.civilian.BorderExpander;
import model.unit.civilian.Builder;
import model.unit.civilian.Explorer;
import model.unit.civilian.Worker;
import model.unit.combat.Archer;
import model.unit.combat.Catapult;
import model.unit.combat.Cavalry;
import model.unit.combat.Swordsman;
import model.combat.enemy.Barbarian;
import model.combat.enemy.Bear;

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
            case BARBARIAN -> new Barbarian();
            case BEAR -> new Bear();
        };
    }
}
