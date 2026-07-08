package model.storage;

public class MaxStorage {

    private int maxWood;
    private int maxStone;
    private int maxIron;
    private int maxFood;


    public MaxStorage(int maxFood, int maxWood, int maxStone, int maxIron){

        this.maxFood = maxFood;
        this.maxWood = maxWood;
        this.maxStone = maxStone;
        this.maxIron = maxIron;

    }


    public int getMaxWood() {
        return maxWood;
    }

    public int getMaxStone() {
        return maxStone;
    }

    public int getMaxIron() {
        return maxIron;
    }

    public int getMaxFood() {
        return maxFood;
    }
}