package model.trade.tribe;

import model.storage.ResourceType;

public class FarmerTrade implements TribeTrade {
    @Override
    public double getRate() {
        return 0.75;
    }

    @Override
    public boolean canReceive(ResourceType type) {
        return type==ResourceType.FOOD;
    }
}
