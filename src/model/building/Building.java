package model.building;

import model.Cost;
import model.Hex;
import model.unit.Worker;

import java.util.ArrayList;
import java.util.List;

public abstract class Building {
    private Hex hex;
    private Cost cost;
    private Cost upkeep;
    private List<Worker> assignedWorkers = new ArrayList<>();
    private int workerCapacity;

    public Building(Hex hex, Cost cost, Cost upkeep, int workerCapacity){
        this.hex = hex;
        this.cost = cost;
        this.upkeep = upkeep;
        this.workerCapacity = workerCapacity;
    }

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

    public boolean assignWorker(Worker worker){
        if(assignedWorkers.size()<workerCapacity && !worker.isAssigned()){
            assignedWorkers.add(worker);
            return true;
        }
        return false;
    }

    public void removeWorker(Worker worker){
        assignedWorkers.remove(worker);
    }

    public int getWorkerCapacity() {
        return workerCapacity;
    }

}
