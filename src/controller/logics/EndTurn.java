package controller.logics;

import model.GameState;
import model.unit.Unit;

public class EndTurn {

    public static void endTurn(GameState gameState){

        gameState.getProduction().recalculate(gameState);

        for(Unit unit: gameState.getUnits()){
            unit.resetActionPointsForNewTurn();
        }

        gameState.getStorage().setFoodQuantity(Math.min(gameState.getStorage().getFoodMax(), gameState.getProduction().getFoodProduction()+gameState.getStorage().getFoodQuantity()));
        gameState.getStorage().setWoodQuantity(Math.min(gameState.getStorage().getWoodMax(), gameState.getProduction().getWoodProduction()+gameState.getStorage().getWoodQuantity()));
        gameState.getStorage().setStoneQuantity(Math.min(gameState.getStorage().getStoneMax(), gameState.getProduction().getStoneProduction()+gameState.getStorage().getStoneQuantity()));
        gameState.getStorage().setIronQuantity(Math.min(gameState.getStorage().getIronMax(), gameState.getProduction().getIronProduction()+gameState.getStorage().getIronQuantity()));

        if (gameState.getStorage().getFoodQuantity() < 0) {
            for (Unit unit : gameState.getUnits()) {
                unit.setActionPoints(Math.max(0, unit.getActionPoints()*2/3));
            }
            gameState.setStarving(true);
        } else {
            gameState.setStarving(false);
        }

        gameState.nextTurn();

    }



}
