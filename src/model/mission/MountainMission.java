package model.mission;

import model.building.tribes.Tribe;
import model.state.GameState;
import model.storage.Cost;

import java.io.Serializable;

public class MountainMission extends Mission implements Serializable {
private static final long serialVersionUID = 1L;

    private final Cost cost = new Cost(15, 0, 10, 0, 0);

    public MountainMission(Tribe tribe) {
        super(tribe, 6, "Send 15 wood and 10 iron |  You get 20 stone");
    }

    @Override
    public boolean isAffordable(GameState gameState) {
        return cost.enoughResources(gameState.getEmpireState().getStorage());
    }

    @Override
    public void check(GameState gameState) {
        if(isAffordable(gameState)) {
            cost.pay(gameState.getEmpireState().getStorage());
            super.setStatus(MissionStatus.DONE);
        }
    }

    @Override
    public void processDone(GameState gameState) {
        if(super.getStatus()==MissionStatus.DONE) {
            gameState.getEmpireState().getStorage().setStoneQuantity(gameState.getEmpireState().getStorage().getStoneQuantity() + 20);
            super.getTribe().changeRelation(super.getTribe().getRelation()+15, gameState.getEmpireState().getAllies());
            super.setStatus(MissionStatus.REWARDED);
        }
    }
}
