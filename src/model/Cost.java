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

    public boolean enoughResources(Storage storage){
        return storage.getWoodQuantity() >= this.getWoodNeeded() && storage.getStoneQuantity() >= this.getStoneNeeded() && storage.getIronQuantity() >= this.getIronNeeded();
    }

    public void pay(Storage storage){
        storage.setIronQuantity(storage.getIronQuantity()-this.getIronNeeded());
        storage.setStoneQuantity(storage.getStoneQuantity()-this.getStoneNeeded());
        storage.setWoodQuantity(storage.getWoodQuantity()-this.getWoodNeeded());
    }

    @Override
    public String toString(){
        return "wood : "+woodNeeded + " | stone : "+stoneNeeded+ " | iron : "+ironNeeded;
    }

}
