package model.building.other;

import model.building.TradingBuilding;
import model.map.Hex;

import java.io.Serializable;

public class TradingCenter extends TradingBuilding implements Serializable {
private static final long serialVersionUID = 1L;
    public TradingCenter(Hex hex) {
        super(hex, null, null, 50);
    }

    @Override
    public double getMultiplier(int quantity) {
        return 0.8;
    }
}
