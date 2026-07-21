package model.building;

import model.map.Hex;

public class TradingCenter extends TradingBuilding {
    public TradingCenter(Hex hex) {
        super(hex, null, null, 50);
    }

    @Override
    public String getBuildingImageName() {
        return "tradingcenterhex";
    }

    @Override
    public double getMultiplier(int quantity) {
        return 0.8;
    }
}
