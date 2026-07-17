package model.building;

import model.TownHallLevel;
import model.map.Hex;
import model.storage.Cost;

public class TradingCenter extends  Building {

    private boolean usedThisTurn;

    public TradingCenter(Hex hex){
        super(hex, null, null, 0, 50);
        this.usedThisTurn = false;
    }

    @Override
    public int getProductionPerWorker() {
        return 0;
    }

    @Override
    public String getBuildingImageName() {
        return "tradingcenterhex";
    }

    public static double getMultiplier() {return 0.8;}

    public boolean isUsedThisTurn() {
        return usedThisTurn;
    }

    public void setUsedThisTurn(boolean usedThisTurn) {
        this.usedThisTurn = usedThisTurn;
    }

}
