package model.unit;

import model.map.Hex;

public abstract class Unit{
    public enum UNIT_TYPE {EXPLORER, BUILDER, BORDER_EXPANDER, WORKER};

    protected UNIT_TYPE unitType;
    protected int positionX;
    protected int positionY;
    protected int actionPoints;
    protected int actionPointsMax;
    protected boolean isAssigned;
    protected boolean isMoving;
    protected int foodConsumptionPerTurn;
    protected Hex hex;
    private Hex targetHex;
    private int maxHP;
    private int HP;


    protected Unit(UNIT_TYPE unitType, int actionPointsMax, int foodConsumptionPerTurn, int maxHP){
        this.unitType = unitType;
        this.actionPointsMax = actionPointsMax;
        this.actionPoints = actionPointsMax;
        this.isAssigned = false;
        this.hex = null;
        this.targetHex = null;
        this.foodConsumptionPerTurn = foodConsumptionPerTurn;
        this.maxHP = maxHP;
        this.HP = maxHP;
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
    public Hex getHex() {
        return hex;
    }
    public void setHex(Hex hex) {
        this.hex = hex;
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
    public boolean isMoving() {
        return isMoving;
    }
    public void setMoving(boolean moving) {
        isMoving = moving;
    }
    public Hex getTargetHex() {
        return targetHex;
    }
    public void setTargetHex(Hex targetHex) {
        this.targetHex = targetHex;
    }
    public boolean isWorker() { return false; }
    public boolean isBuilder() { return false; }
    public boolean isExplorer() { return false; }
    public boolean isBorderExpander() { return false; }
    public int getCharges() { return 0; }
    public void setCharges(int charges) { }
    public int getMaxHP() {return maxHP;}
    public void setMaxHP(int maxHP) {this.maxHP = maxHP;}
    public int getHP() {return HP;}
    public void setHP(int HP) {this.HP = HP;}

    public String getUnitImageName() {
        return unitType.toString().toLowerCase().replace("_", "");
    }

    public void resetActionPointsForNewTurn() {
        this.actionPoints = actionPointsMax;
    }

}

