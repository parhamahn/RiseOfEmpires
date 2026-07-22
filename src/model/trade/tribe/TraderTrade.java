package model.trade.tribe;

import model.storage.ResourceType;

public class TraderTrade implements TribeTrade {
    private double missionBonus = 0.0;

    @Override
    public double getRate() {
        return 0.8 + missionBonus;
    }

    @Override
    public boolean canReceive(ResourceType type) {
        return true;
    }

    public double getMissionBonus() {
        return missionBonus;
    }

    public void setMissionBonus(double missionBonus) {
        this.missionBonus = missionBonus;
    }
}
