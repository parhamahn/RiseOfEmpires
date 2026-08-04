package model.state;

import model.*;
import model.building.*;
import model.building.empire.MilitaryStable;
import model.building.empire.Monument;
import model.building.empire.TownHall;
import model.building.tribes.Tribe;
import model.map.Hex;
import model.map.Map;
import model.mission.Mission;
import model.product.AdjacencyBonus;
import model.product.Production;
import model.queue.*;
import model.storage.Storage;
import model.unit.*;
import model.unit.civilian.Builder;
import model.unit.civilian.Explorer;
import model.unit.civilian.Worker;
import model.unit.combat.Swordsman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmpireState  implements Serializable {
private static final long serialVersionUID = 1L;
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
    private List<Tribe> allies;
    private int warHappinessEffect;
    private List<Mission> missions = new ArrayList<>();

    public EmpireState(Storage storage, Production production) {
        this.storage = storage;
        this.production = production;
        this.maxUnit = DEFAULT_UNIT_CAPACITY;
        this.townHallLevel = TownHallLevel.BASE_CAMP;
        this.queues = new ArrayList<>();
        this.researchedTechnologies = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.season = Season.SeasonType.SPRING;
        this.allies = new  ArrayList<>();
        this.warHappinessEffect = 0;
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

    public void removeBuilding(Building building) {
        for (Unit unit : building.getAssignedWorkers()) {
            unit.setAssigned(false);
        }
        building.getAssignedWorkers().clear();
        building.getHex().setBuilding(null);
        buildings.remove(building);
    }

    public void removeUnit(Unit unit) {
        unit.getHex().getUnits().remove(unit);
        units.remove(unit);
    }

    public void updateTownCapacity() {
        long townCount = buildings.stream().filter(Building::isTown).count();
        this.maxUnit = DEFAULT_UNIT_CAPACITY + (int) townCount;
    }

    public void recalculateProduction(Map map) {
        production.recalculate(this, AdjacencyBonus.checkForAdjacencyBonus(buildings, map));
    }

    public void recalculateHappiness() {
        this.happiness = this.warHappinessEffect;

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

    public boolean canCookUnit(Unit.UNIT_TYPE type) {
        if (type.isEnemy()) return false;
        if (!queues.isEmpty()) return false;
        if (type.getLevel().ordinal() > townHallLevel.ordinal()) return false;
        if (type.getCookCost() != null && !type.getCookCost().enoughResources(storage)) return false;
        if (type.getAttackPower() > 0) {
            if (getCombatUnitCount() >= townHallLevel.getCombatUnitCap()) return false;
            if (townHallHex != null && !townHallHex.canAddUnitType(type)) return false;
        } else {
            if (getCivilUnitCount() >= maxUnit) return false;
        }
        if (type == Unit.UNIT_TYPE.CAVALRY && buildings.stream().noneMatch(b -> b instanceof MilitaryStable)) return false;
        return true;
    }

    public boolean canResearch(Technology tech) {
        if (researchedTechnologies.contains(tech)) return false;
        if (queues.stream().anyMatch(q -> q.getQueueType() == Queue.QUEUE_TYPE.RESEARCH
                && ((ResearchQueue) q).getTechnology() == tech)) return false;
        if (!tech.getCost().enoughResources(storage)) return false;
        if (tech.getTownHallLevel().ordinal() > townHallLevel.ordinal()) return false;
        if (!queues.isEmpty()) return false;
        return true;
    }

    public boolean hasUpgradeQueued() {
        return queues.stream().anyMatch(q -> q.getQueueType() == Queue.QUEUE_TYPE.UPGRADE);
    }

    public List<Tribe> getAllies() {
        return allies;
    }

    public int getWarHappinessEffect() {
        return warHappinessEffect;
    }

    public void setWarHappinessEffect(int warHappinessEffect) {
        this.warHappinessEffect = warHappinessEffect;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void loadFrom(EmpireState loaded){
        this.storage = loaded.getStorage();
        this.production = loaded.getProduction();
        this.townHallLevel = loaded.getTownHallLevel();
        this.townHallHex = loaded.getTownHallHex();
        this.maxUnit = loaded.getMaxUnit();
        this.upkeepDisaster = loaded.isUpkeepDisaster();
        this.happiness = loaded.getHappiness();
        this.buildings = loaded.getBuildings();
        this.units = loaded.getUnits();
        this.isStarving = loaded.isStarving();
        this.queues = loaded.getQueues();
        this.researchedTechnologies = loaded.getResearchedTechnologies();
        this.season = loaded.getSeason();
        this.allies = loaded.getAllies();
        this.warHappinessEffect = loaded.getWarHappinessEffect();
        this.missions = loaded.getMissions();
    }

}
