package model.state;

import model.*;
import model.building.Building;
import model.building.TownHall;
import model.unit.*;

import java.util.ArrayList;
import java.util.List;

public class EmpireState {
    public final static int DEFAULT_UNIT_CAPACITY = 6;

    private Storage storage;
    private Production production;
    private List<Building> buildings;
    private List<Unit> units;
    private boolean isStarving;
    private boolean upkeepDisaster;
    private int maxUnit;
    private int storageLevel;
    private Hex townHallHex;
    private List<CookQueue> unitQueues;

    public EmpireState(Storage storage, Production production) {
        this.storage = storage;
        this.production = production;
        this.maxUnit = DEFAULT_UNIT_CAPACITY;
        this.storageLevel = 1;
        this.unitQueues = new ArrayList<>();
        this.buildings = new ArrayList<>();
    }

    public void init(Map map) {
        Hex thHex = map.getHex(map.getCols() / 2, map.getRows() / 2);
        TownHall townHall = new TownHall(thHex);
        thHex.setBuilding(townHall);
        this.townHallHex = thHex;
        this.buildings.add(townHall);

        units = new ArrayList<>();
        units.add(new Builder());
        units.add(new Builder());
        units.add(new Worker());
        units.add(new Worker());
        units.add(new Explorer());

        for (Unit unit : units) { unit.setHex(townHallHex); }
        townHallHex.getUnits().addAll(units);

        recalculateProduction();
    }

    public void recalculateProduction() {
        production.recalculate(this);
    }

    public Storage getStorage() { return storage; }
    public Production getProduction() { return production; }
    public List<Building> getBuildings() { return buildings; }
    public List<Unit> getUnits() { return units; }
    public boolean isStarving() { return isStarving; }
    public void setStarving(boolean starving) { isStarving = starving; }
    public int getMaxUnit() { return maxUnit; }
    public void setMaxUnit(int maxUnit) { this.maxUnit = maxUnit; }
    public boolean isUpkeepDisaster() { return upkeepDisaster; }
    public void setUpkeepDisaster(boolean upkeepDisaster) { this.upkeepDisaster = upkeepDisaster; }
    public int getStorageLevel() { return storageLevel; }
    public void setStorageLevel(int storageLevel) { this.storageLevel = storageLevel; }
    public Hex getTownHallHex() { return townHallHex; }
    public List<CookQueue> getUnitQueues() { return unitQueues; }
}
