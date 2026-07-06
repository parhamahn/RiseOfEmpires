package model.building;

import model.Cost;
import model.Hex;
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

    public Building(Hex hex, Cost cost, Cost upkeep, int workerCapacity){
        this.hex = hex;
        this.cost = cost;
        this.upkeep = upkeep;
        this.workerCapacity = workerCapacity;
        this.noUpkeepTurns = 0;
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
}
