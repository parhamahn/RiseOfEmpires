package controller.handler.building.townhall;

import model.queue.CookQueue;
import model.state.GameState;
import model.unit.Unit;
import model.unit.UnitFactory;

public class CookUnitHandler {
    private static final int COOK_DELAY = 3;

    public void startCook(GameState gameState, Unit.UNIT_TYPE unitType) {
        Unit unit = UnitFactory.createUnit(unitType);

        if (unitType.getCookCost() != null) {
            if (!unitType.getCookCost().enoughResources(gameState.getEmpireState().getStorage())) {
                return;
            }
            unitType.getCookCost().pay(gameState.getEmpireState().getStorage());
        }

        int totalUnits = gameState.getEmpireState().getUnits().size() + (int) gameState.getEmpireState().getCookQueueCount();
        if (totalUnits >= gameState.getEmpireState().getMaxUnit()) {
            return;
        }
        gameState.getEmpireState().getQueues().add(
                new CookQueue(unit, unitType.getTurnCost(), gameState.getWorldState().getTurnNumber()));
    }
}
