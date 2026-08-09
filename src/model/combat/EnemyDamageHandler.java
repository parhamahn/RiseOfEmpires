package model.combat;

import model.building.tribes.Tribe;
import model.map.Hex;
import model.mission.Mission;
import model.mission.MissionStatus;
import model.mission.WarriorMission;
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
                    gameState.getWorldState().getEnemies().remove(u);
                    u.getHex().getUnits().remove(u);
                    countWarriorKill(gameState, u.getHex());
                }
            }
        }
        return remaining;
    }

    private void countWarriorKill(GameState gameState, Hex killHex) {
        for (Tribe tribe : gameState.getWorldState().getTribes()) {
            Mission mission = tribe.getActiveMission();
            if (mission instanceof WarriorMission
                    && mission.getStatus() == MissionStatus.ACTIVE
                    && gameState.getWorldState().getMap().getDistance(tribe.getHex(), killHex) <= 1) {
                ((WarriorMission) mission).enemyKilled();
            }
        }
    }
}
