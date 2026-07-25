package model.trade.tribe;

import model.storage.ResourceType;

import java.util.ArrayList;
import java.util.List;

public class MountainTrade implements TribeTrade{
    @Override
    public double getRate() {
        return 0.75;
    }

    @Override
    public boolean canProvide(ResourceType type) {
        return type==ResourceType.IRON || type==ResourceType.STONE;
    }

    @Override
    public List<ResourceType> getResourceTypes() {
        List<ResourceType> providedResources =  new ArrayList<>();
        providedResources.add(ResourceType.IRON);
        providedResources.add(ResourceType.STONE);
        return providedResources;
    }
}
