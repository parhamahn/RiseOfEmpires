package model.product;

import model.Season;
import model.building.*;
import model.building.empire.Farm;
import model.building.empire.IronMine;
import model.building.empire.Stable;
import model.building.empire.StoneMine;
import model.map.Hex;
import model.queue.Technology;
import model.state.EmpireState;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;

public class Production {
    private final int DEFAULT_FOOD_PRODUCTION = 9;
    private final int DEFAULT_WOOD_PRODUCTION = 1;
    private final int DEFAULT_STONE_PRODUCTION = 1;
    private final int DEFAULT_IRON_PRODUCTION = 0;

    private int foodProduction;
    private int woodProduction;
    private int stoneProduction;
    private int ironProduction;

    public Production(){
        reset();
    }


    public int getFoodProduction() {
        return foodProduction;
    }
    public int getWoodProduction() {
        return woodProduction;
    }
    public int getStoneProduction() {
        return stoneProduction;
    }
    public int getIronProduction() {
        return ironProduction;
    }


    public void recalculate(EmpireState empire, AdjacencyBonus.ExtraResources bonus){
        this.foodProduction = DEFAULT_FOOD_PRODUCTION;
        this.woodProduction = DEFAULT_WOOD_PRODUCTION;
        this.stoneProduction = DEFAULT_STONE_PRODUCTION;
        this.ironProduction = DEFAULT_IRON_PRODUCTION;

        for (Unit unit : empire.getUnits()) {
            this.foodProduction -= unit.getFoodConsumptionPerTurn();
        }

        List<Building> toRemove = new ArrayList<>();

        int protestPenalty = 0;
        if(empire.getHappiness()<=-3){protestPenalty = 1;}

        int seasonEffect = 0;
        if(empire.getSeason()== Season.SeasonType.SPRING){seasonEffect = 1;}
        if(empire.getSeason()== Season.SeasonType.WINTER){seasonEffect = -1;}

        for (Building building : empire.getBuildings()) {
            int workers = building.getAssignedWorkers().size();
            Hex.ResourceType resourceType = building.getResourceType();

            double steelToolsMultiplier = 1;
            if(empire.getResearchedTechnologies().contains(Technology.STEEL_TOOLS) && (building instanceof StoneMine || building instanceof IronMine)){steelToolsMultiplier = 1.5;}

            if (workers > 0 && resourceType != null && !building.isProducingStopped()) {
                int resourceOnHex = building.getHex().getResources().getOrDefault(resourceType, 0);
                int storageCap = getStorageCap(empire, resourceType);
                int producingResource = (int) Math.min(Math.min(workers * (Math.floor(building.getProductionPerWorker() * steelToolsMultiplier)-protestPenalty), resourceOnHex), storageCap);

                if(building instanceof Farm || building instanceof Stable){producingResource+= seasonEffect;}

                if (resourceOnHex - producingResource > 0) {
                    building.getHex().getResources().put(resourceType, resourceOnHex - producingResource);
                } else {
                    toRemove.add(building);
                    building.getHex().getResources().remove(resourceType);
                    if (building.getHex().getResources().isEmpty()) {
                        building.getHex().setTerrainType(Hex.TerrainType.PLAIN);
                    }
                }

                addProduction(resourceType, producingResource);
            }

            if (this.foodProduction < 0 && empire.getStorage().getFoodQuantity() < 0) {
                empire.setStarving(true);
                for (Unit unit : empire.getUnits()) {
                    unit.setActionPoints(unit.getActionPoints() - 2);
                }
            }

            if (building.getUpkeep() != null) {
                this.woodProduction -= building.getUpkeep().getWoodNeeded();
                this.stoneProduction -= building.getUpkeep().getStoneNeeded();
                this.ironProduction -= building.getUpkeep().getIronNeeded();
            }
        }
        for (Building building : toRemove) {
            empire.removeBuilding(building);
        }

        this.foodProduction += bonus.getExtraFood();
        this.woodProduction += bonus.getExtraWood();
        this.stoneProduction += bonus.getExtraStone();
        this.ironProduction += bonus.getExtraIron();

        if(empire.getHappiness() >= 3){
            this.foodProduction += Math.floor(0.1*this.foodProduction);
            this.woodProduction += Math.floor(0.1*this.woodProduction);
            this.stoneProduction += Math.floor(0.1*this.stoneProduction);
            this.ironProduction += Math.floor(0.1*this.ironProduction);
        }

    }

    private void addProduction(Hex.ResourceType resourceType, int amount) {
        switch (resourceType) {
            case WOOD  -> this.woodProduction += amount;
            case STONE -> this.stoneProduction += amount;
            case IRON  -> this.ironProduction += amount;
            case WHEAT, RICE, COW, SHEEP -> this.foodProduction += amount;
        }
    }

    private int getStorageCap(EmpireState empire, Hex.ResourceType resourceType) {
        return switch (resourceType) {
            case WOOD  -> empire.getStorage().getWoodMax()  - empire.getStorage().getWoodQuantity();
            case STONE -> empire.getStorage().getStoneMax() - empire.getStorage().getStoneQuantity();
            case IRON  -> empire.getStorage().getIronMax()  - empire.getStorage().getIronQuantity();
            case WHEAT, RICE, COW, SHEEP -> empire.getStorage().getFoodMax() - empire.getStorage().getFoodQuantity();
            default -> 0;
        };
    }

    public void reset(){
        this.foodProduction = DEFAULT_FOOD_PRODUCTION;
        this.woodProduction = DEFAULT_WOOD_PRODUCTION;
        this.stoneProduction = DEFAULT_STONE_PRODUCTION;
        this.ironProduction = DEFAULT_IRON_PRODUCTION;
    }

}
