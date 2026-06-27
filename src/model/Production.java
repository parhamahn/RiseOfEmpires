package model;

import model.building.Building;
import model.unit.Unit;

public class Production {
    private int DEFAULT_FOOD_PRODUCTION = 5;
    private int DEFAULT_WOOD_PRODUCTION = 5;
    private int DEFAULT_STONE_PRODUCTION = 5;
    private int DEFAULT_IRON_PRODUCTION = 5;

    public int foodProduction;
    public int woodProduction;
    public int stoneProduction;
    public int ironProduction;

    public Production(){
        this.foodProduction = DEFAULT_FOOD_PRODUCTION;
        this.woodProduction = DEFAULT_WOOD_PRODUCTION;
        this.stoneProduction = DEFAULT_STONE_PRODUCTION;
        this.ironProduction = DEFAULT_IRON_PRODUCTION;
    }

    public int getFoodProduction() {
        return foodProduction;
    }

    public void setFoodProduction(int foodProduction) {
        this.foodProduction = foodProduction;
    }

    public int getWoodProduction() {
        return woodProduction;
    }

    public void setWoodProduction(int woodProduction) {
        this.woodProduction = woodProduction;
    }

    public int getStoneProduction() {
        return stoneProduction;
    }

    public void setStoneProduction(int stoneProduction) {
        this.stoneProduction = stoneProduction;
    }

    public int getIronProduction() {
        return ironProduction;
    }

    public void setIronProduction(int ironProduction) {
        this.ironProduction = ironProduction;
    }

    public void recalculate(GameState gameState){
        this.foodProduction = DEFAULT_FOOD_PRODUCTION;
        this.woodProduction = DEFAULT_WOOD_PRODUCTION;
        this.stoneProduction = DEFAULT_STONE_PRODUCTION;
        this.ironProduction = DEFAULT_IRON_PRODUCTION;

        for(Unit unit: gameState.getUnits()){
            unit.resetActionPointsForNewTurn();
            this.foodProduction -= Unit.FoodPerTurn;
        }

        for (Building building:gameState.getBuildings()){
            if(building.getUpkeep()!=null){
                this.woodProduction-=building.getUpkeep().getWoodNeeded();
                this.stoneProduction-=building.getUpkeep().getStoneNeeded();
                this.ironProduction-=building.getUpkeep().getIronNeeded();
            }
        }

    }

}
