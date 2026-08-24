package model.queue;

import model.map.Hex;
import model.state.GameState;
import model.unit.Unit;

import java.io.Serializable;

public class CookQueue extends Queue implements Serializable {
private static final long serialVersionUID = 1L;

    private Unit unit;
    private Hex targetHex;


    public CookQueue(Unit unit, int turnCost, int startTurn, Hex targetHex){
        super(QUEUE_TYPE.COOK, turnCost, startTurn);
        this.unit = unit;
        this.targetHex = targetHex;
    }


    public Unit getUnit() {
        return unit;
    }

    @Override
    public boolean execute(GameState gameState) {
        Hex target = resolveTarget(gameState);
        if (target == null) return false;
        unit.setHex(target);
        target.getUnits().add(unit);
        gameState.getEmpireState().getUnits().add(unit);
        return false;
    }

    private Hex resolveTarget(GameState gameState) {
        if (isValidTarget(targetHex)) return targetHex;
        for (Hex townHall : gameState.getEmpireState().getTownHallHexes()) {
            if (isValidTarget(townHall)) return townHall;
        }
        return null;
    }

    private boolean isValidTarget(Hex hex) {
        return hex != null && hex.canAddUnitType(unit.getUnitType());
    }

    @Override
    public void cancel(GameState gameState) {
    }

    @Override
    public String getDescription() {
        return "Cooking " + formatted(unit.getUnitType());
    }
}
