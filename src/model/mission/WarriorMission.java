package model.mission;

import model.building.tribes.Tribe;
import model.state.GameState;
import model.unit.combat.Swordsman;
import model.unit.Unit;

import java.io.Serializable;


public class WarriorMission extends Mission implements Serializable {
private static final long serialVersionUID = 1L;
    public WarriorMission(Tribe tribe) {

        super(tribe, 8, "Kill 3 enemies in tribe's area | You get 1 swordsman");
    }

    private int killedEnemies;

    @Override
    public boolean isAffordable(GameState gameState) {
        return killedEnemies >= 3;
    }

    public void enemyKilled() {
        killedEnemies++;
    }

    public int getKilledEnemies() {
        return killedEnemies;
    }

    @Override
    public void check(GameState gameState) {
        if(isAffordable(gameState)){
            super.setStatus(MissionStatus.DONE);
        }
    }

    @Override
    public void processDone(GameState gameState) {
        if(super.getStatus().equals(MissionStatus.DONE)){
            super.getTribe().changeRelation(super.getTribe().getRelation()+20, gameState.getEmpireState().getAllies());
            for(int i=0;i<3;i++){
                Unit unit = new Swordsman();
                gameState.getEmpireState().getUnits().add(unit);
                unit.setHex(gameState.getEmpireState().getTownHallHex());
                gameState.getEmpireState().getTownHallHex().getUnits().add(unit);
            }
            super.setStatus(MissionStatus.REWARDED);
        }
    }
}
