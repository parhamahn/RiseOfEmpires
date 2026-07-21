package model.combat;

import model.state.GameState;
import model.unit.Unit;

import java.util.List;

public abstract class DamageHandler {

    protected DamageHandler next;

    public DamageHandler setNext(DamageHandler next) {
        this.next = next;
        return next;
    }

    public void handle(List<Unit> defenders, int damage, GameState gameState) {
        int remaining = applyDamage(defenders, damage, gameState);
        if (remaining > 0 && next != null) {
            next.handle(defenders, remaining, gameState);
        }
    }

    protected abstract int applyDamage(List<Unit> defenders, int damage, GameState gameState);

    protected static int damageUnit(List<Unit> defenders, Unit target, int damage, GameState gameState) {
        int remaining = damage;
        target.setHealth(target.getHealth() - 1);
        remaining--;
        if (target.getHealth() <= 0) {
            defenders.remove(target);
            gameState.getEmpireState().removeUnit(target);
        }
        return remaining;
    }
}
