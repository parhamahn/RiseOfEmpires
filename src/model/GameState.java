package model;

import model.building.Building;
import model.building.Town;
import model.building.TownHall;
import model.unit.*;
import view.MapPanel;

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
    private int tunrNumber;
    private int maxUnit;
    private int unitCount;
    private Unit selectedUnit;


    public GameState(Storage storage, Production production, Map map){
        this.storage = storage;
        this.production = production;
        this.selectedHex = null;
        this.selectedUnit = null;
        this.map = map;
        this.tunrNumber = 0;
        this.isStarving = false;
        this.maxUnit = 6;
        this.unitCount = 5;
        buildings = new ArrayList<>();

        Hex townHallHex = map.getHex(map.getCols()/2, map.getRows()/2);
        TownHall townHall = new TownHall(townHallHex);
        townHallHex.setBuilding(townHall);
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
        this.tunrNumber+=1;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public boolean isStarving() {
        return isStarving;
    }

    public void setStarving(boolean starving) {
        isStarving = starving;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getTunrNumber() {
        return tunrNumber;
    }

    public void setTunrNumber(int tunrNumber) {
        this.tunrNumber = tunrNumber;
    }

    public int getMaxUnit() {
        return maxUnit;
    }

    public void setMaxUnit(int maxUnit) {
        this.maxUnit = maxUnit;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public void updateUnits(){
        int extra = 0;
        int count = 0;
        for (Building building:buildings){
            if(building instanceof Town)extra+=2;
        }
        for(Unit unit:units){
            count+=1;
        }
        maxUnit=6+extra;
        unitCount=count;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }
}
