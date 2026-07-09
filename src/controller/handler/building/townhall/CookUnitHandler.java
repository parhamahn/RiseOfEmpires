package controller.handler.building.townhall;

import model.queue.CookQueue;
import model.state.GameState;
import model.unit.*;

public class CookUnitHandler {
    private static final int COOK_DELAY = 3;

    public void startCook(GameState gameState, Unit.UNIT_TYPE unitType) {
        Unit unit = switch (unitType) {
            case WORKER -> new Worker();
            case BORDER_EXPANDER -> new BorderExpander();
            case BUILDER -> new Builder();
            case EXPLORER -> new Explorer();
        };

        int totalUnits = gameState.getEmpireState().getUnits().size() + (int) gameState.getEmpireState().getCookQueueCount();
        if (totalUnits >= gameState.getEmpireState().getMaxUnit()) {
            return;
        }

        gameState.getEmpireState().getQueues().add(
                new CookQueue(unit, COOK_DELAY, gameState.getWorldState().getTurnNumber()));
    }
}
