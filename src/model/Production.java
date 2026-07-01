package model;

import model.building.*;
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

            if(building instanceof LumberMill){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){if(unit.getActionPoints()>0)workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(Hex.ResourceType.WOOD, 0);
                int producingResource = Math.min(workingWorkers*LumberMill.productingPerUnit, resourceOnHex);

                building.getHex().getResources().put(Hex.ResourceType.WOOD, resourceOnHex-producingResource);

                this.woodProduction += producingResource;


            }

            if(building instanceof StoneMine){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){if(unit.getActionPoints()>0)workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(Hex.ResourceType.STONE, 0);
                int producingResource = Math.min(workingWorkers*StoneMine.productingPerUnit, resourceOnHex);

                building.getHex().getResources().put(Hex.ResourceType.STONE, resourceOnHex-producingResource);

                this.stoneProduction += producingResource;


            }

            if(building instanceof IronMine){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){if(unit.getActionPoints()>0)workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(Hex.ResourceType.IRON, 0);
                int producingResource = Math.min(workingWorkers*IronMine.productingPerUnit, resourceOnHex);

                building.getHex().getResources().put(Hex.ResourceType.IRON, resourceOnHex-producingResource);

                this.ironProduction += producingResource;


            }

            if(building instanceof Farm){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){if(unit.getActionPoints()>0)workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(((Farm) building).getFarming(), 0);
                int producingResource = Math.min(workingWorkers*Farm.productingPerUnit, resourceOnHex);

                building.getHex().getResources().put(((Farm) building).getFarming(), resourceOnHex-producingResource);

                this.foodProduction += producingResource;


            }



            if(building.getUpkeep()!=null){
                this.woodProduction-=building.getUpkeep().getWoodNeeded();
                this.stoneProduction-=building.getUpkeep().getStoneNeeded();
                this.ironProduction-=building.getUpkeep().getIronNeeded();
            }
        }
    }

}
