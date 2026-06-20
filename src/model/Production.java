package model;

public class Production {
    private int DEFAULT_FOOD_PRODUCTION = 5;

    public int food_production;
    public int wood_production;
    public int stone_production;
    public int iron_production;

    public Production(){
        this.food_production = DEFAULT_FOOD_PRODUCTION;
        this.wood_production = 0;
        this.stone_production = 0;
        this.iron_production = 0;
    }

    public int getFood_production() {
        return food_production;
    }

    public void setFood_production(int food_production) {
        this.food_production = food_production;
    }

    public int getWood_production() {
        return wood_production;
    }

    public void setWood_production(int wood_production) {
        this.wood_production = wood_production;
    }

    public int getStone_production() {
        return stone_production;
    }

    public void setStone_production(int stone_production) {
        this.stone_production = stone_production;
    }

    public int getIron_production() {
        return iron_production;
    }

    public void setIron_production(int iron_production) {
        this.iron_production = iron_production;
    }
}
