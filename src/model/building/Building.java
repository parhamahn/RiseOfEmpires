package model.building;

import model.storage.Cost;
import model.map.Hex;
import model.Task;
import model.unit.Worker;

import java.util.ArrayList;
import java.util.List;

public abstract class Building {
    public static int actionPointToAssign = 1;

    private Hex hex;
    private Cost cost;
    private Cost upkeep;
    private List<Worker> assignedWorkers = new ArrayList<>();
    private int workerCapacity;
    private int noUpkeepTurns;
    private int maxHP;
    private int HP;
    private boolean producingStopped;


    public Building(Hex hex, Cost cost, Cost upkeep, int workerCapacity, int maxHP){
        this.hex = hex;
        this.cost = cost;
        this.upkeep = upkeep;
        this.workerCapacity = workerCapacity;
        this.noUpkeepTurns = 0;
        this.maxHP = maxHP;
        this.HP = maxHP;
    }


    public abstract int getProductionPerWorker();
    public Hex.ResourceType getResourceType() { return null; }
    public abstract String getBuildingImageName();
    public boolean isTown() { return false; }
    public boolean isTownHall() { return false; }
    public Hex getHex() {
        return hex;
    }
    public Cost getCost(){
        return cost;
    }
    public Cost getUpkeep() {
        return upkeep;
    }
    public List<Worker> getAssignedWorkers() {
        return assignedWorkers;
    }
    public int getWorkerCapacity() {
        return workerCapacity;
    }
    public int getNoUpkeepTurns() {
        return noUpkeepTurns;
    }
    public void setNoUpkeepTurns(int noUpkeepTurns) {
        this.noUpkeepTurns = noUpkeepTurns;
    }
    public int getHP() {return HP;}
    public void setHP(int HP) {this.HP = HP;}
    public int getMaxHP() {return maxHP;}
    public void setMaxHP(int maxHP) {this.maxHP = maxHP;}
    public boolean isProducingStopped() {
        return producingStopped;
    }
    public void setProducingStopped(boolean producingStopped) {this.producingStopped = producingStopped;}

    public static Cost getBuildCost(Task.TASK task) {
        return switch (task) {
            case FARM        -> Farm.getBuildCost();
            case LUMBER_MILL -> LumberMill.getBuildCost();
            case STONE_MINE  -> StoneMine.getBuildCost();
            case IRON_MINE   -> IronMine.getBuildCost();
            case STABLE      -> Stable.getBuildCost();
            case TOWN        -> Town.getBuildCost();
            case MONUMENT    -> Monument.getBuildCost();
            case BAZAAR -> Bazaar.getBuildCost();
            case DOCK -> Dock.getBuildCost();
            case MILITARY_STABLE -> MilitaryStable.getBuildCost();
            case WALL -> Wall.getBuildCost();
            case ROAD -> Road.getBuildCost();
        };
    }
}
