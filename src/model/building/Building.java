package model.building;

import model.MapStructure;
import model.Task;
import model.map.Hex;
import model.storage.Cost;

import java.io.Serializable;

public abstract class Building extends MapStructure implements Serializable {
private static final long serialVersionUID = 1L;
    public static int actionPointToAssign = 1;

    private Cost cost;
    private Cost upkeep;
    private int noUpkeepTurns;
    private boolean producingStopped;

    public Building(Hex hex, Cost cost, Cost upkeep, int maxHP){
        super(hex, maxHP);
        this.cost = cost;
        this.upkeep = upkeep;
        this.noUpkeepTurns = 0;
    }

    public boolean isTown() { return false; }
    public boolean isApothecary(){return false;}

    public Cost getCost(){
        return cost;
    }

    public Cost getUpkeep() {
        return upkeep;
    }

    public int getNoUpkeepTurns() {
        return noUpkeepTurns;
    }

    public void setNoUpkeepTurns(int noUpkeepTurns) {
        this.noUpkeepTurns = noUpkeepTurns;
    }

    public boolean isProducingStopped() {
        return producingStopped;
    }

    public void setProducingStopped(boolean producingStopped) {
        this.producingStopped = producingStopped;
    }

    public static Cost getBuildCost(Task.TASK task) {
        return BuildingFactory.getBuildCost(task);
    }
}
