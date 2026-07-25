package model.trade.tribe;
import model.storage.ResourceType;

import java.util.List;


public interface TribeTrade {
    double getRate();
    boolean canProvide(ResourceType type);
    List<ResourceType> getResourceTypes();
}