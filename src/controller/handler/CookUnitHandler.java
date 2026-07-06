package controller.handler;

import model.CookQueue;
import model.state.GameState;
import model.unit.*;

import java.util.ArrayList;
import java.util.List;

public class CookUnitHandler {
    private static final int COOK_DELAY = 3;

    public void startCook(GameState gameState, Unit.UNIT_TYPE unitType) {
        Unit unit = switch (unitType) {
            case WORKER -> new Worker();
            case BORDER_EXPANDER -> new BorderExpander();
            case BUILDER -> new Builder();
            case EXPLORER -> new Explorer();
        };

        int totalUnits = gameState.getEmpireState().getUnits().size() + gameState.getEmpireState().getUnitQueues().size();
        if (totalUnits >= gameState.getEmpireState().getMaxUnit()) {
            return;
        }

        gameState.getEmpireState().getUnitQueues().add(new CookQueue(unit, gameState.getWorldState().getTurnNumber() + COOK_DELAY));
    }

    public void checkCooks(GameState gameState) {
        List<CookQueue> toRemove = new ArrayList<>();
        for (CookQueue cookQueue : gameState.getEmpireState().getUnitQueues()) {
            if (cookQueue.getTurnToCook() - gameState.getWorldState().getTurnNumber() == 0) {
                cookQueue.getUnit().setHex(gameState.getEmpireState().getTownHallHex());
                gameState.getEmpireState().getTownHallHex().getUnits().add(cookQueue.getUnit());
                gameState.getEmpireState().getUnits().add(cookQueue.getUnit());
                toRemove.add(cookQueue);
            }
            gameState.getEmpireState().recalculateProduction();
        }
        gameState.getEmpireState().getUnitQueues().removeAll(toRemove);
    }
}
