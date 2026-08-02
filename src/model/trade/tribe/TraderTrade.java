package model.trade.tribe;

import model.storage.ResourceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TraderTrade  implements TribeTrade, Serializable {
    private double missionBonus = 0.0;

    @Override
    public double getRate() {
        return 0.8 + missionBonus;
    }

    @Override
    public boolean canProvide(ResourceType type) {
        return true;
    }

    @Override
    public List<ResourceType> getResourceTypes() {
        List<ResourceType> providedResources =  new ArrayList<>();
        providedResources.add(ResourceType.FOOD);
        providedResources.add(ResourceType.WOOD);
        providedResources.add(ResourceType.STONE);
        providedResources.add(ResourceType.IRON);
        return providedResources;
    }

    public double getMissionBonus() {
        return missionBonus;
    }

    public void setMissionBonus(double missionBonus) {
        this.missionBonus = missionBonus;
    }
}
