package model.trade.tribe;

import model.storage.ResourceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FarmerTrade implements TribeTrade, Serializable {
    @Override
    public double getRate() {
        return 0.75;
    }

    @Override
    public boolean canProvide(ResourceType type) {
        return type==ResourceType.FOOD;
    }

    @Override
    public List<ResourceType> getResourceTypes() {
        List<ResourceType> providedResources =  new ArrayList<>();
        providedResources.add(ResourceType.FOOD);
        return providedResources;
    }
}
