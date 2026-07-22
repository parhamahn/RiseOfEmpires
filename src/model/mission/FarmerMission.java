package model.mission;

import model.building.tribes.Tribe;
import model.state.GameState;
import model.storage.Cost;

public class FarmerMission extends Mission {

    private final Cost cost = new Cost(20, 10, 0, 0, 0);

    public FarmerMission(Tribe tribe) {
        super(tribe, 5);
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
            gameState.getEmpireState().getStorage().setFoodQuantity(gameState.getEmpireState().getStorage().getFoodQuantity() + 30);
            super.getTribe().changeRelation(super.getTribe().getRelation()+15);
            super.setStatus(MissionStatus.REWARDED);
        }
    }
}
