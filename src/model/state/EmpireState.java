package model.state;

import model.*;
import model.building.Building;
import model.building.Monument;
import model.building.TownHall;
import model.building.Wall;
import model.map.Hex;
import model.map.Map;
import model.product.AdjacencyBonus;
import model.product.Production;
import model.queue.Queue;
import model.queue.Technology;
import model.storage.Storage;
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
    private Hex townHallHex;
    private List<Queue> queues;
    private List<Technology> researchedTechnologies;
    private TownHallLevel townHallLevel;
    private int happiness;
    private Season.SeasonType season;
    private List<Wall> walls;

    public EmpireState(Storage storage, Production production) {
        this.storage = storage;
        this.production = production;
        this.maxUnit = DEFAULT_UNIT_CAPACITY;
        this.townHallLevel = TownHallLevel.BASE_CAMP;
        this.queues = new ArrayList<>();
        this.researchedTechnologies = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.season = Season.SeasonType.SPRING;
        this.walls = new ArrayList<>();
    }

    public Season.SeasonType getSeason() {
        return season;
    }

    public void setSeason(Season.SeasonType season) {
        this.season = season;
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

        recalculateProduction(map);
        recalculateHappiness();
    }

    public void recalculateProduction(Map map) {
        production.recalculate(this, AdjacencyBonus.checkForAdjacencyBonus(buildings, map));
    }

    public List<Wall> getWalls() { return walls; }

    public boolean hasWall(Hex hex1, Hex hex2) {
        for (Wall wall : walls) {
            if (wall.connects(hex1, hex2)) return true;
        }
        return false;
    }

    public void recalculateHappiness() {
        this.happiness = 0;

        for(Building building:buildings){
            if(building.isTown()){this.happiness -= 1;}
            if(building instanceof Monument){this.happiness += 2;}
        }

        if(getCombatUnitCount()==townHallLevel.getCombatUnitCap()){this.happiness -= 1;}

        for(Unit unit: townHallHex.getUnits()){
            if(unit.isCombatUnit()){
                this.happiness += 1;
                break;
            }
        }
    }

    public int getCivilUnitCount(){
        int count = 0;
        for (Unit unit:units){
            if (!unit.isCombatUnit()){count+=1;}
        }
        return count;
    }

    public int getCombatUnitCount(){
        int count = 0;
        for (Unit unit:units){
            if (unit.isCombatUnit()){count+=1;}
        }
        return count;
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
    public TownHallLevel getTownHallLevel() {return townHallLevel;}
    public void setTownHallLevel(TownHallLevel townHallLevel) {this.townHallLevel = townHallLevel;}
    public Hex getTownHallHex() { return townHallHex; }
    public List<Queue> getQueues() { return queues; }
    public List<Technology> getResearchedTechnologies() { return researchedTechnologies; }
    public long getCookQueueCount() {return queues.stream().filter(q -> q.getQueueType() == Queue.QUEUE_TYPE.COOK).count();}
    public int getHappiness() {return happiness;}
}
