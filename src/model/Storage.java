package model;

public class Storage {
    public int DEFAULT_WOOD_MAX_STORAGE = 75;
    public int DEFAULT_STONE_MAX_STORAGE = 50;
    public int DEFAULT_IRON_MAX_STORAGE = 25;
    public int DEFAULT_FOOD_MAX_STORAGE = 50;
    public int DEFAULT_WOOD_QUANTITY = 50;
    public int DEFAULT_STONE_QUANTITY = 25;
    public int DEFAULT_IRON_QUANTITY = 0;
    public int DEFAULT_FOOD_QUANTITY = 25;

    private int woodQuantity;
    private int stoneQuantity;
    private int ironQuantity;
    private int foodQuantity;
    private int woodMax;
    private int stoneMax;
    private int ironMax;
    private int foodMax;

    public Storage() {
        this.woodQuantity = DEFAULT_WOOD_QUANTITY;
        this.stoneQuantity = DEFAULT_STONE_QUANTITY;
        this.ironQuantity = DEFAULT_IRON_QUANTITY;
        this.foodQuantity = DEFAULT_FOOD_QUANTITY;
        this.foodMax = DEFAULT_FOOD_MAX_STORAGE;
        this.stoneMax = DEFAULT_STONE_MAX_STORAGE;
        this.ironMax = DEFAULT_IRON_MAX_STORAGE;
        this.woodMax = DEFAULT_WOOD_MAX_STORAGE;
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

    public void setFoodMax(int foodMax) {
        this.foodMax = foodMax;
    }

    public int getIronMax() {
        return ironMax;
    }

    public void setIronMax(int ironMax) {
        this.ironMax = ironMax;
    }

    public int getStoneMax() {
        return stoneMax;
    }

    public void setStoneMax(int stoneMax) {
        this.stoneMax = stoneMax;
    }

    public int getWoodMax() {
        return woodMax;
    }

    public void setWoodMax(int woodMax) {
        this.woodMax = woodMax;
    }

    public void upgradeStorage(int x){
        this.foodMax = DEFAULT_FOOD_MAX_STORAGE * x;
        this.woodMax = DEFAULT_WOOD_MAX_STORAGE * x;
        this.stoneMax = DEFAULT_STONE_MAX_STORAGE * x;
        this.ironMax = DEFAULT_IRON_MAX_STORAGE * x;
    }
}