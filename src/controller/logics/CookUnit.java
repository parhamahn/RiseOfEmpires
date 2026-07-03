package controller.logics;

import model.CookQueue;
import model.GameState;
import model.unit.*;

import java.util.ArrayList;
import java.util.List;

public class CookUnit {

    public static void startCook(GameState gameState, Unit.UNIT_TYPE unitType){
        Unit unit = null;
        switch (unitType){
            case WORKER -> unit = new Worker();
            case BORDER_EXPANDER -> unit = new BorderExpander();
            case BUILDER -> unit = new Builder();
            case EXPLORER -> unit = new Explorer();
        }

        int totalUnits = gameState.getUnits().size() + gameState.getUnitQueues().size();
        if (totalUnits >= gameState.getMaxUnit()) {
            return;
        }

        gameState.getUnitQueues().add(new CookQueue(unit, gameState.getTunrNumber()+3));
    }

    public static void checkCooks(GameState gameState){
        List<CookQueue> toRemove = new ArrayList<>();
        for (CookQueue cookQueue:gameState.getUnitQueues()){
            if(cookQueue.getTurnToCook()-gameState.getTunrNumber()==0){
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
