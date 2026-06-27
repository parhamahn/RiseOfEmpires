package model.unit;

import model.Hex;
import model.Storage;

public abstract class Unit{
    public enum UNIT_TYPE {EXPLORER, BUILDER, BORDER_EXPANDER, WORKER};
    public static final int FoodPerTurn = 1;

    protected UNIT_TYPE unitType;
    protected int positionX;
    protected int positionY;
    protected int actionPoints;
    protected int actionPointsMax;
    protected boolean isWorking;
    public boolean isAssigned;
    protected int foodConsumptionPerTurn;


    protected Unit(UNIT_TYPE unitType, int actionPointsMax, int foodConsumptionPerTurn){
        this.unitType = unitType;
        this.actionPointsMax = actionPointsMax;
        this.actionPoints = actionPointsMax;
        this.isAssigned = false;
        this.isWorking = false;
        this.foodConsumptionPerTurn = foodConsumptionPerTurn;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public boolean consumeFood(Storage storage){
        if (storage.getFoodQuantity() >= foodConsumptionPerTurn) {
            storage.setFoodQuantity(storage.getFoodQuantity() - foodConsumptionPerTurn);
            return true;
        }
        return false;
    }

    public void resetActionPointsForNewTurn() {
        this.actionPoints = actionPointsMax;
    }

    public void moveToPosition(int newX, int newY) {

        this.positionX = newX;
        this.positionY = newY;

    }

    public int getMovementAPCost(Hex hex) {
        return switch (hex.getTerrainType()){
            case MEADOW -> 1;
            case PLAIN -> 1;
            case FOREST -> 2;
            case MOUNTAIN -> 4;
        };
    }

    public UNIT_TYPE getUnitType(){
        return unitType;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public void setActionPoints(int actionPoints) {
        this.actionPoints = actionPoints;
    }

    public int getActionPointsMax() {
        return actionPointsMax;
    }

    public void setActionPointsMax(int actionPointsMax) {
        this.actionPointsMax = actionPointsMax;
    }
}
