package model;

import model.building.Building;
import model.building.TownHall;
import model.unit.Builder;
import model.unit.Explorer;
import model.unit.Unit;
import model.unit.Worker;

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


    public GameState(Storage storage, Production production, Map map){
        this.storage = storage;
        this.production = production;
        this.selectedHex = null;
        this.map = map;
        this.tunrNumber = 0;
        this.isStarving = false;
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
}
