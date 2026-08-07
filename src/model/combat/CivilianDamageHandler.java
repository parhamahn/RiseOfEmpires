package model.combat;

import model.state.GameState;
import model.unit.Unit;

import java.util.List;

public class CivilianDamageHandler extends DamageHandler {

    @Override
    protected int applyDamage(List<Unit> defenders, int damage, GameState gameState) {
        int remaining = damage;
        var it = defenders.iterator();
        while (it.hasNext() && remaining > 0) {
            Unit u = it.next();
            if (!u.isCombatUnit() && !u.getUnitType().isEnemy()) {
                remaining = damageUnit(u, remaining);
                if (isDead(u)) {
                    it.remove();
                    if (u.isWorker() && u.getHex().getBuilding() != null) {
                        u.getHex().getBuilding().getAssignedWorkers().remove(u);
                    }
                    gameState.getEmpireState().removeUnit(u);
                }
            }
        }
        return remaining;
    }
}
