package model.product;

import model.Season;
import model.building.*;
import model.building.empire.*;
import model.building.tribes.BeachTribe;
import model.building.tribes.FarmerTribe;
import model.map.Hex;
import model.map.Map;
import model.queue.Technology;
import model.state.EmpireState;
import model.unit.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Production implements Serializable {
private static final long serialVersionUID = 1L;
    private final int DEFAULT_FOOD_PRODUCTION = 6;
    private final int DEFAULT_WOOD_PRODUCTION = 1;
    private final int DEFAULT_STONE_PRODUCTION = 0;
    private final int DEFAULT_IRON_PRODUCTION = 0;

    private int foodProduction;
    private int woodProduction;
    private int stoneProduction;
    private int ironProduction;
    private Season.SeasonType season = Season.SeasonType.SPRING;
    private Random rng = new Random();

    public Production(){
        reset();
    }

    public void setRng(Random rng) {
        this.rng = rng;
    }

    public Season.SeasonType getSeason() {
        return season;
    }

    public void setSeason(Season.SeasonType season) {
        this.season = season;
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


    public void recalculate(EmpireState empire, AdjacencyBonus.ExtraResources bonus, Map map){
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
        if(season == Season.SeasonType.SPRING){seasonEffect = 1;}
        if(season == Season.SeasonType.WINTER){seasonEffect = -1;}

        for (Building building : empire.getBuildings()) {
            double steelToolsMultiplier = 1;
            if(empire.getResearchedTechnologies().contains(Technology.STEEL_TOOLS) && (building instanceof StoneMine || building instanceof IronMine)){steelToolsMultiplier = 1.5;}

            if (building instanceof ProducingBuilding producing) {
                int workers = producing.getAssignedWorkers().size();
                Hex.ResourceType resourceType = producing.getResourceType();

                if (workers > 0 && resourceType != null && !producing.isProducingStopped()) {
                    Hex resourceHex = producing.getHex();
                    int resourceOnHex = resourceHex.getResources().getOrDefault(resourceType, 0);

                    if(producing instanceof Dock){
                        List<Hex> fishNeighbors = map.getNeighbourHexes(producing.getHex()).stream()
                                .filter(Objects::nonNull)
                                .filter(hex -> hex.getTerrainType() == Hex.TerrainType.SEA)
                                .filter(hex -> hex.getResources().getOrDefault(resourceType, 0) > 0)
                                .toList();
                        if(!fishNeighbors.isEmpty()){
                            resourceHex = fishNeighbors.get(rng.nextInt(fishNeighbors.size()));
                            resourceOnHex = resourceHex.getResources().getOrDefault(resourceType, 0);
                        }
                    }

                    int storageCap = getStorageCap(empire, resourceType);
                    int producingResource = (int) Math.min(Math.min(workers * (Math.floor(producing.getProductionPerWorker() * steelToolsMultiplier)-protestPenalty), resourceOnHex), storageCap);

                    if(producing instanceof Farm || producing instanceof Stable || producing instanceof Dock){
                        boolean farmerAlly = empire.getAllies().stream().anyMatch(tribe ->  tribe instanceof FarmerTribe);
                        if(farmerAlly){
                            producingResource = (int)Math.floor(producingResource*FarmerTribe.FOOD_PRODUCTION_BOOST);
                        }
                        producingResource+= seasonEffect;
                    }

                    if (resourceOnHex - producingResource > 0) {
                        resourceHex.getResources().put(resourceType, resourceOnHex - producingResource);
                    } else {
                        toRemove.add(building);
                        resourceHex.getResources().remove(resourceType);
                        if (!(producing instanceof Dock) && resourceHex.getResources().isEmpty()) {
                            resourceHex.setTerrainType(Hex.TerrainType.PLAIN);
                        }
                    }

                    addProduction(resourceType, producingResource);
                }
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

        boolean beachAlly = empire.getAllies().stream().anyMatch(BeachTribe.class::isInstance);
        if (beachAlly) {
            this.foodProduction += 4;
        }

    }

    private void addProduction(Hex.ResourceType resourceType, int amount) {
        switch (resourceType) {
            case WOOD  -> this.woodProduction += amount;
            case STONE -> this.stoneProduction += amount;
            case IRON  -> this.ironProduction += amount;
            case WHEAT, RICE, COW, SHEEP, FISH -> this.foodProduction += amount;
        }
    }

    private int getStorageCap(EmpireState empire, Hex.ResourceType resourceType) {
        return switch (resourceType) {
            case WOOD  -> empire.getStorage().getWoodMax()  - empire.getStorage().getWoodQuantity();
            case STONE -> empire.getStorage().getStoneMax() - empire.getStorage().getStoneQuantity();
            case IRON  -> empire.getStorage().getIronMax()  - empire.getStorage().getIronQuantity();
            case WHEAT, RICE, COW, SHEEP, FISH -> empire.getStorage().getFoodMax() - empire.getStorage().getFoodQuantity();
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
