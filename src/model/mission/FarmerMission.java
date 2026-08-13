package model.mission;

import model.building.tribes.Tribe;
import model.state.GameState;
import model.storage.Cost;

import java.io.Serializable;

public class FarmerMission extends Mission implements Serializable {
private static final long serialVersionUID = 1L;

    private final Cost cost = new Cost(20, 10, 0, 0, 0);

    public FarmerMission(Tribe tribe) {
        super(tribe, 5, "Send 20 wood and 10 stone | You get 30 food");
    }

    @Override
    public boolean isAffordable(GameState gameState) {
        return cost.enoughResources(gameState.getEmpireState().getStorage());
    }

    @Override
    public boolean canReward(GameState gameState) {
        return gameState.getEmpireState().getStorage().getFoodQuantity() + 30
                <= gameState.getEmpireState().getStorage().getFoodMax();
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
            gameState.getEmpireState().getStorage().setFoodQuantity(gameState.getEmpireState().getStorage().getFoodQuantity() + 30);
            super.getTribe().changeRelation(super.getTribe().getRelation()+15, gameState.getEmpireState().getAllies());
            super.setStatus(MissionStatus.REWARDED);
        }
    }
}
