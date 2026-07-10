package model.storage;

public class Storage {
    public int DEFAULT_WOOD_MAX_STORAGE = 100;
    public int DEFAULT_STONE_MAX_STORAGE = 100;
    public int DEFAULT_IRON_MAX_STORAGE = 100;
    public int DEFAULT_FOOD_MAX_STORAGE = 100;
    public int DEFAULT_WOOD_QUANTITY = 50;
    public int DEFAULT_STONE_QUANTITY = 25;
    public int DEFAULT_IRON_QUANTITY = 10;
    public int DEFAULT_FOOD_QUANTITY = 20;

    private int woodQuantity;
    private int stoneQuantity;
    private int ironQuantity;
    private int foodQuantity;
    private int woodMax;
    private int stoneMax;
    private int ironMax;
    private int foodMax;


    public Storage() {
        reset();
    }


    public int getWoodQuantity() {
        return woodQuantity;
    }
    public void setWoodQuantity(int woodQuantity) {
        this.woodQuantity = woodQuantity;
    }
    public int getStoneQuantity() {
        return stoneQuantity;
    }
    public void setStoneQuantity(int stoneQuantity) {
        this.stoneQuantity = stoneQuantity;
    }
    public int getIronQuantity() {
        return ironQuantity;
    }
    public void setIronQuantity(int ironQuantity) {
        this.ironQuantity = ironQuantity;
    }
    public int getFoodQuantity() {
        return foodQuantity;
    }
    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
    public int getFoodMax() {
        return foodMax;
    }
    public int getIronMax() {
        return ironMax;
    }
    public int getStoneMax() {
        return stoneMax;
    }
    public int getWoodMax() {
        return woodMax;
    }


    public void upgradeStorage(int x){
        this.foodMax = DEFAULT_FOOD_MAX_STORAGE * x;
        this.woodMax = DEFAULT_WOOD_MAX_STORAGE * x;
        this.stoneMax = DEFAULT_STONE_MAX_STORAGE * x;
        this.ironMax = DEFAULT_IRON_MAX_STORAGE * x;
    }

    public void reset(){
        this.woodQuantity = DEFAULT_WOOD_QUANTITY;
        this.stoneQuantity = DEFAULT_STONE_QUANTITY;
        this.ironQuantity = DEFAULT_IRON_QUANTITY;
        this.foodQuantity = DEFAULT_FOOD_QUANTITY;
        this.foodMax = DEFAULT_FOOD_MAX_STORAGE;
        this.stoneMax = DEFAULT_STONE_MAX_STORAGE;
        this.ironMax = DEFAULT_IRON_MAX_STORAGE;
        this.woodMax = DEFAULT_WOOD_MAX_STORAGE;
    }
}