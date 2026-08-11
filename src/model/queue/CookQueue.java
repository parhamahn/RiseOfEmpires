package model.queue;

import model.state.GameState;
import model.unit.Unit;

import java.io.Serializable;

public class CookQueue extends Queue implements Serializable {
private static final long serialVersionUID = 1L;

    private Unit unit;


    public CookQueue(Unit unit, int turnCost, int startTurn){
        super(QUEUE_TYPE.COOK, turnCost, startTurn);
        this.unit = unit;
    }


    public Unit getUnit() {
        return unit;
    }

    @Override
    public boolean execute(GameState gameState) {
        model.map.Hex townHallHex = gameState.getEmpireState().getTownHallHex();
        if (townHallHex.canAddUnitType(unit.getUnitType())) {
            unit.setHex(townHallHex);
            townHallHex.getUnits().add(unit);
            gameState.getEmpireState().getUnits().add(unit);
        }
        return false;
    }

    @Override
    public void cancel(GameState gameState) {
    }

    @Override
    public String getDescription() {
        return "Cooking " + formatted(unit.getUnitType());
    }
}
