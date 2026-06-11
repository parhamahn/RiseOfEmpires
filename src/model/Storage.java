package model;

public class Storage {
    public int DEFAULT_WOOD_MAX_STORAGE = 100;
    public int DEFAULT_STONE_MAX_STORAGE = 100;
    public int DEFAULT_IRON_MAX_STORAGE = 100;
    public int DEFAULT_FOOD_MAX_STORAGE = 100;
    public int DEFAULT_WOOD_QUANTITY = 50;
    public int DEFAULT_STONE_QUANTITY = 25;
    public int DEFAULT_IRON_QUANTITY = 0;
    public int DEFAULT_FOOD_QUANTITY = 75;

    private int wood_quantity;
    private int stone_quantity;
    private int iron_quantity;
    private int food_quantity;

    public int getWood_quantity() {
        return wood_quantity;
    }

    public void setWood_quantity(int wood_quantity) {
        this.wood_quantity = wood_quantity;
    }

    public int getStone_quantity() {
        return stone_quantity;
    }

    public void setStone_quantity(int stone_quantity) {
        this.stone_quantity = stone_quantity;
    }

    public int getIron_quantity() {
        return iron_quantity;
    }

    public void setIron_quantity(int iron_quantity) {
        this.iron_quantity = iron_quantity;
    }

    public int getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(int food_quantity) {
        this.food_quantity = food_quantity;
    }
}
