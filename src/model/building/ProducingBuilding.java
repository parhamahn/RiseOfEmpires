package model.building;

import model.map.Hex;
import model.storage.Cost;
import model.unit.civilian.Worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ProducingBuilding extends Building implements Serializable {
private static final long serialVersionUID = 1L;

    private List<Worker> assignedWorkers = new ArrayList<>();
    private int workerCapacity;

    protected ProducingBuilding(Hex hex, Cost cost, Cost upkeep, int workerCapacity, int maxHP) {
        super(hex, cost, upkeep, maxHP);
        this.workerCapacity = workerCapacity;
    }

    public abstract int getProductionPerWorker();

    public Hex.ResourceType getResourceType() { return null; }

    public List<Worker> getAssignedWorkers() {
        return assignedWorkers;
    }

    public int getWorkerCapacity() {
        return workerCapacity;
    }
}
