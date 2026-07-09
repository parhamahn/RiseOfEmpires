package model.storage;

public class Cost {
    private int woodNeeded;
    private int stoneNeeded;
    private int ironNeeded;
    private int APNeeded;
    private int foodNeeded;


    public Cost(int woodNeeded, int stoneNeeded, int ironNeeded, int foodNeeded, int APNeeded){
        this.woodNeeded = woodNeeded;
        this.stoneNeeded = stoneNeeded;
        this.ironNeeded = ironNeeded;
        this.foodNeeded = foodNeeded;
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
    public int getFoodNeeded() {return foodNeeded;}

    public boolean enoughResources(Storage storage){
        return storage.getWoodQuantity() >= this.getWoodNeeded() && storage.getStoneQuantity() >= this.getStoneNeeded() && storage.getIronQuantity() >= this.getIronNeeded() && storage.getFoodQuantity()>=this.foodNeeded;
    }

    public boolean enoughAP(int AP){
        return AP >= this.APNeeded;
    }

    public void pay(Storage storage){
        storage.setFoodQuantity(storage.getFoodQuantity()-this.foodNeeded);
        storage.setIronQuantity(storage.getIronQuantity()-this.getIronNeeded());
        storage.setStoneQuantity(storage.getStoneQuantity()-this.getStoneNeeded());
        storage.setWoodQuantity(storage.getWoodQuantity()-this.getWoodNeeded());
    }

    @Override
    public String toString(){
        return "wood : "+woodNeeded + " | stone : "+stoneNeeded+ " | iron : "+ironNeeded + " | food : "+foodNeeded;
    }

}
