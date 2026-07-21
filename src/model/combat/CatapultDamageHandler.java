package model.combat;

import model.state.GameState;
import model.unit.Unit;

import java.util.List;

public class CatapultDamageHandler extends DamageHandler {

    @Override
    protected int applyDamage(List<Unit> defenders, int damage, GameState gameState) {
        int remaining = damage;
        var it = defenders.iterator();
        while (it.hasNext() && remaining > 0) {
            Unit u = it.next();
            if (u.getUnitType() == Unit.UNIT_TYPE.CATAPULT) {
                remaining = damageUnit(defenders, u, remaining, gameState);
            }
        }
        return remaining;
    }
}
