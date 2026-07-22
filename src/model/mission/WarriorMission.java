package model.mission;

import model.building.tribes.Tribe;
import model.state.GameState;
import model.unit.Swordsman;
import model.unit.Unit;


public class WarriorMission extends Mission{
    public WarriorMission(Tribe tribe) {

        super(tribe, 8);
    }

    @Override
    public boolean isAffordable(GameState gameState) {
        return false;
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
            super.getTribe().changeRelation(super.getTribe().getRelation()+20);
            for(int i=0;i<3;i++){
                Unit unit = new Swordsman();
                gameState.getEmpireState().getUnits().add(unit);
                unit.setHex(gameState.getEmpireState().getTownHallHex());
                gameState.getEmpireState().getTownHallHex().getUnits().add(unit);
            }
        }
    }
}
