package model;

import model.building.Building;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;

public class Production {
    private int DEFAULT_FOOD_PRODUCTION = 9;
    private int DEFAULT_WOOD_PRODUCTION = 3;
    private int DEFAULT_STONE_PRODUCTION = 2;
    private int DEFAULT_IRON_PRODUCTION = 1;

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

        for (Unit unit : gameState.getUnits()) {
            this.foodProduction -= unit.getFoodConsumptionPerTurn();
        }

        List<Building> toRemove = new ArrayList<>();

        for (Building building : gameState.getBuildings()) {
            int workers = building.getAssignedWorkers().size();
            Hex.ResourceType resourceType = building.getResourceType();

            if (workers > 0 && resourceType != null) {
                int resourceOnHex = building.getHex().getResources().getOrDefault(resourceType, 0);
                int storageCap = getStorageCap(gameState, resourceType);
                int producingResource = Math.min(Math.min(workers * building.getProductionPerWorker(), resourceOnHex), storageCap);

                if (resourceOnHex - producingResource > 0) {
                    building.getHex().getResources().put(resourceType, resourceOnHex - producingResource);
                } else {
                    toRemove.add(building);
                    building.getHex().getResources().remove(resourceType);
                    for (Unit unit : building.getHex().getUnits()) {
                        unit.setAssigned(false);
                    }
                    if (building.getHex().getResources().isEmpty()) {
                        building.getHex().setTerrainType(Hex.TerrainType.PLAIN);
                    }
                    building.getHex().setBuilding(null);
                }

                addProduction(resourceType, producingResource);
            }

            if (this.foodProduction < 0 && gameState.getStorage().getFoodQuantity() < 0) {
                gameState.setStarving(true);
                for (Unit unit : gameState.getUnits()) {
                    unit.setActionPoints(unit.getActionPoints() - 2);
                }
            }

            if (building.getUpkeep() != null) {
                this.woodProduction -= building.getUpkeep().getWoodNeeded();
                this.stoneProduction -= building.getUpkeep().getStoneNeeded();
                this.ironProduction -= building.getUpkeep().getIronNeeded();
            }
        }
        gameState.getBuildings().removeAll(toRemove);
    }

    private void addProduction(Hex.ResourceType resourceType, int amount) {
        switch (resourceType) {
            case WOOD  -> this.woodProduction += amount;
            case STONE -> this.stoneProduction += amount;
            case IRON  -> this.ironProduction += amount;
            case WHEAT, RICE, COW, SHEEP -> this.foodProduction += amount;
        }
    }

    private int getStorageCap(GameState gameState, Hex.ResourceType resourceType) {
        return switch (resourceType) {
            case WOOD  -> gameState.getStorage().getWoodMax()  - gameState.getStorage().getWoodQuantity();
            case STONE -> gameState.getStorage().getStoneMax() - gameState.getStorage().getStoneQuantity();
            case IRON  -> gameState.getStorage().getIronMax()  - gameState.getStorage().getIronQuantity();
            case WHEAT, RICE, COW, SHEEP -> gameState.getStorage().getFoodMax() - gameState.getStorage().getFoodQuantity();
            default -> 0;
        };
    }

}
