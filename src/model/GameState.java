package model;

import model.building.Building;
import model.building.TownHall;
import model.unit.*;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Storage storage;
    private Production production;
    private Hex selectedHex;
    private List<Building> buildings;
    private List<Unit> units;
    private Map map;
    private boolean isStarving;
    private boolean upkeepDisaster;
    private int turnNumber;
    private int maxUnit;
    private Unit selectedUnit;
    private int storageLevel;
    private Hex townHallHex;
    private List<CookQueue> unitQueues;


    public GameState(Storage storage, Production production, Map map){
        this.storage = storage;
        this.production = production;
        this.selectedHex = null;
        this.selectedUnit = null;
        this.map = map;
        this.turnNumber = 1;
        this.isStarving = false;
        this.upkeepDisaster=false;
        this.maxUnit = 6;
        this.storageLevel = 1;
        this.unitQueues = new ArrayList<>();
        buildings = new ArrayList<>();

        Hex townHallHex = map.getHex(map.getCols()/2, map.getRows()/2);
        TownHall townHall = new TownHall(townHallHex);
        townHallHex.setBuilding(townHall);
        this.townHallHex = townHallHex;
        buildings.add(townHall);


        units = new ArrayList<>();
        units.add(new Builder());
        units.add(new Builder());
        units.add(new Worker());
        units.add(new Worker());
        units.add(new Explorer());

        for (Unit unit:units){
            unit.setHex(townHallHex);
        }
        townHallHex.getUnits().addAll(units);


        this.production.recalculate(this);
    }


    public Hex getSelectedHex() {
        return selectedHex;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setSelectedHex(Hex selectedHex) {
        this.selectedHex = selectedHex;
    }

    public Map getMap() {
        return map;
    }

    public Storage getStorage() {
        return storage;
    }

    public Production getProduction() {
        return production;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void nextTurn(){
        this.turnNumber++;
    }

    public boolean isStarving() {
        return isStarving;
    }

    public void setStarving(boolean starving) {
        isStarving = starving;
    }

    public int getTurnNumber() {
        return turnNumber;
    }


    public int getMaxUnit() {
        return maxUnit;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public boolean isUpkeepDisaster() {
        return upkeepDisaster;
    }

    public void setUpkeepDisaster(boolean upkeepDisaster) {
        this.upkeepDisaster = upkeepDisaster;
    }

    public int getStorageLevel() {
        return storageLevel;
    }

    public void setStorageLevel(int storageLevel) {
        this.storageLevel = storageLevel;
    }

    public Hex getTownHallHex() {
        return townHallHex;
    }

    public void updateTownCapacity(){
        int townCount = 0;
        for (Building building : buildings) {
            if (building.isTown()) {
                townCount++;
            }
        }
        maxUnit = 6 + townCount;
    }

    public List<CookQueue> getUnitQueues() {
        return unitQueues;
    }
}
