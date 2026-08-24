package controller.handler.building.townhall;

import model.queue.CookQueue;
import model.state.GameState;
import model.unit.Unit;
import model.unit.UnitFactory;

public class CookUnitHandler {
    private static final int COOK_DELAY = 3;

    public void startCook(GameState gameState, Unit.UNIT_TYPE unitType) {
        if (!gameState.getEmpireState().canCookUnit(unitType)) return;

        Unit unit = UnitFactory.createUnit(unitType);
        unit.setOwnerID(gameState.resolveActivePlayerId());

        if (unitType.getCookCost() != null) {
            unitType.getCookCost().pay(gameState.getEmpireState().getStorage());
        }

        gameState.getEmpireState().getQueues().add(
                new CookQueue(unit, unitType.getTurnCost(), gameState.getWorldState().getTurnNumber()));
    }
}
