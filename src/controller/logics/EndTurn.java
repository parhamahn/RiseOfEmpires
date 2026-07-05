package controller.logics;

import model.CookQueue;
import model.GameState;
import model.building.*;
import model.unit.Unit;
import view.WarningDialogs;

import java.util.Random;

public class EndTurn {

    public static void endTurn(GameState gameState){

        gameState.getProduction().recalculate(gameState);

        for(Unit unit: gameState.getUnits()){
            unit.resetActionPointsForNewTurn();
        }

        gameState.getStorage().setFoodQuantity(Math.max(Math.min(gameState.getStorage().getFoodMax(), gameState.getProduction().getFoodProduction()+gameState.getStorage().getFoodQuantity()), 0));
        gameState.getStorage().setWoodQuantity(Math.max(Math.min(gameState.getStorage().getWoodMax(), gameState.getProduction().getWoodProduction()+gameState.getStorage().getWoodQuantity()), 0));
        gameState.getStorage().setStoneQuantity(Math.max(Math.min(gameState.getStorage().getStoneMax(), gameState.getProduction().getStoneProduction()+gameState.getStorage().getStoneQuantity()), 0));
        gameState.getStorage().setIronQuantity(Math.max(Math.min(gameState.getStorage().getIronMax(), gameState.getProduction().getIronProduction()+gameState.getStorage().getIronQuantity()), 0));

        if (gameState.getStorage().getFoodQuantity() == 0 && gameState.getProduction().getFoodProduction()<0) {
            for (Unit unit : gameState.getUnits()) {
                unit.setActionPoints(Math.max(0, unit.getActionPoints()*2/3));
            }
            for(CookQueue cookQueue:gameState.getUnitQueues()){
                cookQueue.setTurnToCook(cookQueue.getTurnToCook()+1);
            }
            gameState.setStarving(true);
        } else {
            gameState.setStarving(false);
        }


        boolean anyDamagesToBuilding = false;
        for(Building building:gameState.getBuildings()){
            if (building instanceof Farm || building instanceof IronMine || building instanceof Stable || building instanceof StoneMine){
                if(!building.getUpkeep().enoughResources(gameState.getStorage())){
                    Random random = new Random();
                    boolean damageBuilding = random.nextDouble() <= 0.3;
                    if(damageBuilding){
                        anyDamagesToBuilding = true;
                        building.setNoUpkeepTurns(building.getNoUpkeepTurns()+1);
                        if(building.getNoUpkeepTurns()==3){
                            building.getHex().setBuilding(null);
                            for(Unit unit:building.getAssignedWorkers())unit.setAssigned(false);
                            building.getAssignedWorkers().clear();
                            gameState.getBuildings().remove(building);
                        }
                    }
                }
            }
        }
        gameState.setUpkeepDisaster(anyDamagesToBuilding);
        gameState.updateTownCapacity();
        gameState.nextTurn();

    }



}
