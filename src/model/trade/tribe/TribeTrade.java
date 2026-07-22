package model.trade.tribe;
import model.storage.ResourceType;


public interface TribeTrade {
    double getRate();
    boolean canReceive(ResourceType type);
}