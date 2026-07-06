package controller.handler;

import model.CookQueue;
import model.GameState;
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

        int totalUnits = gameState.getUnits().size() + gameState.getUnitQueues().size();
        if (totalUnits >= gameState.getMaxUnit()) {
            return;
        }

        gameState.getUnitQueues().add(new CookQueue(unit, gameState.getTurnNumber() + COOK_DELAY));
    }

    public void checkCooks(GameState gameState) {
        List<CookQueue> toRemove = new ArrayList<>();
        for (CookQueue cookQueue : gameState.getUnitQueues()) {
            if (cookQueue.getTurnToCook() - gameState.getTurnNumber() == 0) {
                cookQueue.getUnit().setHex(gameState.getTownHallHex());
                gameState.getTownHallHex().getUnits().add(cookQueue.getUnit());
                gameState.getUnits().add(cookQueue.getUnit());
                toRemove.add(cookQueue);
            }
            gameState.getProduction().recalculate(gameState);
        }
        gameState.getUnitQueues().removeAll(toRemove);
    }
}
