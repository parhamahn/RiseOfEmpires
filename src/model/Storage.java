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
    private int wood_max;
    private int stone_max;
    private int iron_max;
    private int food_max;

    public Storage(){
        this.wood_quantity = DEFAULT_WOOD_QUANTITY;
        this.stone_quantity = DEFAULT_STONE_QUANTITY;
        this.iron_quantity = DEFAULT_IRON_QUANTITY;
        this.food_quantity = DEFAULT_FOOD_QUANTITY;
        this.food_max = DEFAULT_FOOD_MAX_STORAGE;
        this.stone_max = DEFAULT_STONE_MAX_STORAGE;
        this.iron_max = DEFAULT_IRON_MAX_STORAGE;
        this.wood_max = DEFAULT_WOOD_MAX_STORAGE;
    }

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

    public int getFood_max() {
        return food_max;
    }

    public void setFood_max(int food_max) {
        this.food_max = food_max;
    }

    public int getIron_max() {
        return iron_max;
    }

    public void setIron_max(int iron_max) {
        this.iron_max = iron_max;
    }

    public int getStone_max() {
        return stone_max;
    }

    public void setStone_max(int stone_max) {
        this.stone_max = stone_max;
    }

    public int getWood_max() {
        return wood_max;
    }

    public void setWood_max(int wood_max) {
        this.wood_max = wood_max;
    }
}
