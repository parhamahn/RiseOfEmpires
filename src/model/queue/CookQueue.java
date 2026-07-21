package model.queue;

import model.state.GameState;
import model.unit.Unit;

public class CookQueue extends Queue {

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
        unit.setHex(gameState.getEmpireState().getTownHallHex());
        gameState.getEmpireState().getTownHallHex().getUnits().add(unit);
        gameState.getEmpireState().getUnits().add(unit);
        return false;
    }
}
