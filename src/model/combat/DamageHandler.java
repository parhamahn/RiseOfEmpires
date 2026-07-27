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




    protected static int damageUnit(Unit target, int damage) {
        int remaining = damage;
        target.setHealth(target.getHealth() - 1);
        remaining--;
        return remaining;
    }

    protected static boolean isDead(Unit target) {
        return target.getHealth() <= 0;
    }
}