package model.building;

import model.map.Hex;
import model.storage.Cost;

public abstract class TradingBuilding extends Building {
    private boolean usedThisTurn;

    protected TradingBuilding(Hex hex, Cost cost, Cost upkeep, int maxHP) {
        super(hex, cost, upkeep, 0, maxHP);
        this.usedThisTurn = false;
    }

    public boolean isUsedThisTurn() {
        return usedThisTurn;
    }

    public void setUsedThisTurn(boolean usedThisTurn) {
        this.usedThisTurn = usedThisTurn;
    }

    @Override
    public int getProductionPerWorker() {
        return 0;
    }

    public abstract double getMultiplier(int quantity);
}
