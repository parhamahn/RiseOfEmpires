package model.mission;

import model.building.tribes.TraderTribe;
import model.building.tribes.Tribe;
import model.state.GameState;
import model.trade.tribe.TraderTrade;

public class TraderMission extends  Mission{

    public TraderMission(Tribe tribe) {
        super(tribe, 10);
    }

    @Override
    public boolean isAffordable(GameState gameState) {
        return false;
    }

    @Override
    public void check(GameState gameState) {
        if(isAffordable(gameState)) {
            super.setStatus(MissionStatus.DONE);
        }
    }

    @Override
    public void processDone(GameState gameState) {
        if(super.getStatus().equals(MissionStatus.DONE)) {
            super.getTribe().changeRelation(super.getTribe().getRelation()+20);
            TraderTribe traderTribe = (TraderTribe)super.getTribe();
            TraderTrade tradingStrategy = (TraderTrade) traderTribe.getTradeStrategy();
            tradingStrategy.setMissionBonus(0.1);
        }
    }
}
