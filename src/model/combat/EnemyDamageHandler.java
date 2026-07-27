package model.combat;

import model.state.GameState;
import model.unit.Unit;

import java.util.List;

public class EnemyDamageHandler extends DamageHandler {

    @Override
    protected int applyDamage(List<Unit> defenders, int damage, GameState gameState) {
        int remaining = damage;
        var it = defenders.iterator();
        while (it.hasNext() && remaining > 0) {
            Unit u = it.next();
            if (u.getUnitType().isEnemy()) {
                remaining = damageUnit(u, remaining);
                if (isDead(u)) {
                    it.remove();
                    gameState.getEmpireState().removeUnit(u);
                }
            }
        }
        return remaining;
    }
}
