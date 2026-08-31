package model.storage;

import model.product.Production;

import java.io.Serializable;

public class Storage implements Serializable {
private static final long serialVersionUID = 1L;
    public int DEFAULT_WOOD_MAX_STORAGE = 100;
    public int DEFAULT_STONE_MAX_STORAGE = 100;
    public int DEFAULT_IRON_MAX_STORAGE = 100;
    public int DEFAULT_FOOD_MAX_STORAGE = 100;
    public int DEFAULT_WOOD_QUANTITY = 100;
    public int DEFAULT_STONE_QUANTITY = 100;
    public int DEFAULT_IRON_QUANTITY = 100;
    public int DEFAULT_FOOD_QUANTITY = 100;

    private int woodQuantity;
    private int stoneQuantity;
    private int ironQuantity;
    private int foodQuantity;
    private int woodMax;
    private int stoneMax;
    private int ironMax;
    private int foodMax;
    private int teleportItemCount;
    private int actionItemCount;
    private int combatItemCount;


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

    public int getTeleportItemCount() {
        return teleportItemCount;
    }

    public int getActionItemCount() {
        return actionItemCount;
    }

    public int getCombatItemCount() {
        return combatItemCount;
    }

    public int getQuantity(ResourceType type) {
        return switch (type) {
            case WOOD -> woodQuantity;
            case STONE -> stoneQuantity;
            case IRON -> ironQuantity;
            case FOOD -> foodQuantity;
        };
    }

    public void consumeTeleportItem(){if(this.teleportItemCount>0)this.teleportItemCount -= 1;}
    public void consumeActionItem(){if(this.actionItemCount>0)this.actionItemCount -= 1;}
    public void consumeCombatItem(){if(this.combatItemCount>0)this.combatItemCount -= 1;}

    public void produceTeleportItem(){this.teleportItemCount+=1;}
    public void produceActionItem(){this.actionItemCount+=1;}
    public void produceCombatItem(){this.combatItemCount+=1;}

    public void setQuantity(ResourceType type, int amount) {
        switch (type) {
            case WOOD -> woodQuantity = amount;
            case STONE -> stoneQuantity = amount;
            case IRON -> ironQuantity = amount;
            case FOOD -> foodQuantity = amount;
        }
    }

    public int getMax(ResourceType type) {
        return switch (type) {
            case WOOD -> woodMax;
            case STONE -> stoneMax;
            case IRON -> ironMax;
            case FOOD -> foodMax;
        };
    }

    public void upgradeStorage(MaxStorage maxStorage) {
        woodMax = maxStorage.getMaxWood();
        stoneMax = maxStorage.getMaxStone();
        ironMax = maxStorage.getMaxIron();
        foodMax = maxStorage.getMaxFood();
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
        this.combatItemCount = 2;
        this.actionItemCount = 2;
        this.teleportItemCount = 2;
    }

    public void applyProduction(Production production) {
        for (ResourceType type : ResourceType.values()) {
            int current = getQuantity(type);
            int produced = switch (type) {
                case WOOD -> production.getWoodProduction();
                case STONE -> production.getStoneProduction();
                case IRON -> production.getIronProduction();
                case FOOD -> production.getFoodProduction();
            };
            int max = getMax(type);
            setQuantity(type, Math.max(Math.min(max, current + produced), 0));
        }
    }
}