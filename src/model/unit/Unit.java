package model.unit;

import model.Direction;
import model.Hex;
import model.Storage;
import view.MapPanel;

public abstract class Unit{
    public enum UNIT_TYPE {EXPLORER, BUILDER, BORDER_EXPANDER, WORKER};
    public static final int FoodPerTurn = 1;
    public static final int SPEED = 2;

    protected UNIT_TYPE unitType;
    protected int positionX;
    protected int positionY;
    protected int actionPoints;
    protected int actionPointsMax;
    protected boolean isWorking;
    protected boolean isAssigned;
    protected boolean isMoving;
    protected int foodConsumptionPerTurn;
    protected Hex hex;
    protected Hex destination;
    protected int destinationX;
    protected int destinationY;
    protected Direction direction;


    protected Unit(UNIT_TYPE unitType, int actionPointsMax, int foodConsumptionPerTurn){
        this.unitType = unitType;
        this.actionPointsMax = actionPointsMax;
        this.actionPoints = actionPointsMax;
        this.isAssigned = false;
        this.isWorking = false;
        this.hex = null;
        this.destination = null;
        this.direction = null;
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

    public Hex getDestination() {
        return destination;
    }

    public void setDestination(Hex destination) {
        this.destination = destination;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public void setDestinationX(int destinationX) {
        this.destinationX = destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }

    public void setUnitType(UNIT_TYPE unitType) {
        this.unitType = unitType;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public int getFoodConsumptionPerTurn() {
        return foodConsumptionPerTurn;
    }

    public void setFoodConsumptionPerTurn(int foodConsumptionPerTurn) {
        this.foodConsumptionPerTurn = foodConsumptionPerTurn;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void startMovingTo(Hex target, int targetX, int targetY){
        destinationX = targetX;
        destinationY = targetY;

        double dist = Math.hypot(targetX - positionX, targetY - positionY);
        this.direction = new Direction(
                (targetX - positionX) / dist * SPEED,
                (targetY - positionY) / dist * SPEED
        );
        this.isMoving = true;
        this.destination = target;
    }

    public boolean step() {
        positionX += (int) direction.getDx();
        positionY += (int) direction.getDy();
        if (Math.hypot(destinationX - positionX, destinationY - positionY) < SPEED) {
            positionX = destinationX;
            positionY = destinationY;
            return true;
        }
        return false;
    }

}

