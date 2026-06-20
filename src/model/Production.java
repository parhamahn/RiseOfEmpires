package model;

public class Production {
    private int DEFAULT_FOOD_PRODUCTION = 5;

    public int foodProduction;
    public int woodProduction;
    public int stoneProduction;
    public int ironProduction;

    public Production(){
        this.foodProduction = DEFAULT_FOOD_PRODUCTION;
        this.woodProduction = 0;
        this.stoneProduction = 0;
        this.ironProduction = 0;
    }

    public int getFoodProduction() {
        return foodProduction;
    }

    public void setFoodProduction(int foodProduction) {
        this.foodProduction = foodProduction;
    }

    public int getWoodProduction() {
        return woodProduction;
    }

    public void setWoodProduction(int woodProduction) {
        this.woodProduction = woodProduction;
    }

    public int getStoneProduction() {
        return stoneProduction;
    }

    public void setStoneProduction(int stoneProduction) {
        this.stoneProduction = stoneProduction;
    }

    public int getIronProduction() {
        return ironProduction;
    }

    public void setIronProduction(int ironProduction) {
        this.ironProduction = ironProduction;
    }
}
