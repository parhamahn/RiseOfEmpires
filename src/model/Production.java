package model;

import model.building.*;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;

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

            List<Building> toRemove = new ArrayList<>();

            if(building instanceof LumberMill){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(Hex.ResourceType.WOOD, 0);
                int producingResource = Math.min(workingWorkers*LumberMill.productingPerUnit, resourceOnHex);

                if(resourceOnHex-producingResource!=0) {
                    building.getHex().getResources().put(Hex.ResourceType.WOOD, resourceOnHex - producingResource);
                }else{
                    toRemove.add(building);
                    building.getHex().getResources().remove(Hex.ResourceType.WOOD);
                    for(Unit unit:building.getHex().getUnits()){
                        unit.setAssigned(false);
                    }
                    building.getHex().setTerrainType(Hex.TerrainType.PLAIN);
                    building.getHex().setBuilding(null);
                }

                this.woodProduction += producingResource;

            }

            if(building instanceof StoneMine){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(Hex.ResourceType.STONE, 0);
                int producingResource = Math.min(workingWorkers*StoneMine.productingPerUnit, resourceOnHex);

                if(resourceOnHex-producingResource!=0) {
                    building.getHex().getResources().put(Hex.ResourceType.STONE, resourceOnHex - producingResource);
                }else{
                    toRemove.add(building);
                    building.getHex().getResources().remove(Hex.ResourceType.STONE);
                    for(Unit unit:building.getHex().getUnits()){
                        unit.setAssigned(false);
                    }
                    if(building.getHex().getResources().isEmpty()){
                        building.getHex().setTerrainType(Hex.TerrainType.PLAIN);
                    }
                    building.getHex().setBuilding(null);
                }
                this.stoneProduction += producingResource;


            }

            if(building instanceof IronMine){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(Hex.ResourceType.IRON, 0);
                int producingResource = Math.min(workingWorkers*IronMine.productingPerUnit, resourceOnHex);

                if(resourceOnHex-producingResource!=0) {
                    building.getHex().getResources().put(Hex.ResourceType.IRON, resourceOnHex - producingResource);
                }else{
                    toRemove.add(building);
                    building.getHex().getResources().remove(Hex.ResourceType.IRON);
                    for(Unit unit:building.getHex().getUnits()){
                        unit.setAssigned(false);
                    }
                    if(building.getHex().getResources().isEmpty()){
                        building.getHex().setTerrainType(Hex.TerrainType.PLAIN);
                    }
                    building.getHex().setBuilding(null);
                }
                this.ironProduction += producingResource;


            }

            if(building instanceof Farm){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(((Farm) building).getFarming(), 0);
                int producingResource = Math.min(workingWorkers*Farm.productingPerUnit, resourceOnHex);

                if(resourceOnHex-producingResource!=0) {
                    building.getHex().getResources().put(((Farm) building).getFarming(), resourceOnHex - producingResource);
                }else{
                    toRemove.add(building);
                    building.getHex().getResources().remove(((Farm) building).getFarming());
                    for(Unit unit:building.getHex().getUnits()){
                        unit.setAssigned(false);
                    }
                    if(building.getHex().getResources().isEmpty()){
                        building.getHex().setTerrainType(Hex.TerrainType.PLAIN);
                    }
                    building.getHex().setBuilding(null);
                }
                this.foodProduction += producingResource;
            }

            if(building instanceof Stable){
                int workingWorkers = 0;
                for (Unit unit:building.getAssignedWorkers()){workingWorkers+=1;}

                int resourceOnHex = building.getHex().getResources().getOrDefault(((Stable) building).getBreeding(), 0);
                int producingResource = Math.min(workingWorkers*Stable.productingPerUnit, resourceOnHex);

                if(resourceOnHex-producingResource!=0) {
                    building.getHex().getResources().put(((Stable) building).getBreeding(), resourceOnHex - producingResource);
                }else{
                    toRemove.add(building);
                    building.getHex().getResources().remove(((Stable) building).getBreeding());
                    for(Unit unit:building.getHex().getUnits()){
                        unit.setAssigned(false);
                    }
                    if(building.getHex().getResources().isEmpty()){
                        building.getHex().setTerrainType(Hex.TerrainType.PLAIN);
                    }
                    building.getHex().setBuilding(null);
                }
                this.foodProduction += producingResource;
            }

            gameState.getBuildings().removeAll(toRemove);

            if(this.foodProduction<0 && gameState.getStorage().getFoodQuantity()<0) {
                gameState.setStarving(true);
                for(Unit unit:gameState.getUnits()){
                    unit.setActionPoints(unit.getActionPoints()-2);
                }
            }

            if(building.getUpkeep()!=null){
                this.woodProduction-=building.getUpkeep().getWoodNeeded();
                this.stoneProduction-=building.getUpkeep().getStoneNeeded();
                this.ironProduction-=building.getUpkeep().getIronNeeded();
            }
        }
    }

}
