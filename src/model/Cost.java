package model;

public class Cost {
    private int woodNeeded;
    private int stoneNeeded;
    private int ironNeeded;
    private int APNeeded;

    public Cost(int woodNeeded, int stoneNeeded, int ironNeeded, int APNeeded){
        this.woodNeeded = woodNeeded;
        this.stoneNeeded = stoneNeeded;
        this.ironNeeded = ironNeeded;
        this.APNeeded = APNeeded;
    }

    public int getWoodNeeded() {
        return woodNeeded;
    }

    public int getIronNeeded() {
        return ironNeeded;
    }

    public int getStoneNeeded() {
        return stoneNeeded;
    }

    public int getAPNeeded() {
        return APNeeded;
    }

    public boolean enoughResources(Cost cost, Storage storage){
        return storage.getWood_quantity() >= cost.getWoodNeeded() && storage.getStone_quantity() >= cost.getStoneNeeded() && storage.getIron_quantity() >= cost.getIronNeeded();
    }
}
