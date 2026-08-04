package model.unit;

import model.TownHallLevel;
import model.map.Hex;
import model.storage.Cost;

import java.io.Serializable;

public abstract class Unit implements Serializable {
private static final long serialVersionUID = 1L;

    public enum UNIT_TYPE {
        EXPLORER(null, 3, TownHallLevel.BASE_CAMP, 0, 0, 1, false),
        BUILDER(null, 3, TownHallLevel.BASE_CAMP, 0, 0, 1, false),
        BORDER_EXPANDER(null, 3, TownHallLevel.BASE_CAMP, 0, 0, 1, false),
        WORKER(null, 3, TownHallLevel.BASE_CAMP, 0, 0, 1, false),
        ARCHER(new Cost(10, 5, 0, 20, 0), 3, TownHallLevel.SETTLEMENT, 2, 6, 1, false),
        CAVALRY(new Cost(10, 0, 2, 20, 0), 3, TownHallLevel.SETTLEMENT, 1, 8, 2, false),
        SWORDSMAN(new Cost(10, 0, 0, 20, 0), 3, TownHallLevel.BASE_CAMP, 1, 10, 1, false),
        CATAPULT(new Cost(15, 20, 0, 20, 0), 3, TownHallLevel.CAPITAL, 2, 20, 1, false),
        BARBARIAN(null, 0, TownHallLevel.BASE_CAMP, 1, 8, 2, true),
        BEAR(null, 0, TownHallLevel.BASE_CAMP, 1, 35, 2, true);

        private final Cost cookCost;
        private final int turnCost;
        private final TownHallLevel level;
        private final int attackRadius;
        private final int attackPower;
        private final int health;
        private final boolean isEnemy;

        UNIT_TYPE(Cost cookCost, int turnCost, TownHallLevel level, int attackRadius, int attackPower, int health, boolean isEnemy) {
            this.cookCost = cookCost;
            this.turnCost = turnCost;
            this.level = level;
            this.attackRadius = attackRadius;
            this.attackPower = attackPower;
            this.health = health;
            this.isEnemy = isEnemy;
        }

        public Cost getCookCost() { return cookCost; }
        public int getTurnCost() { return turnCost; }
        public TownHallLevel getLevel() { return level; }
        public int getAttackRadius() {return attackRadius;};
        public int getHealth() {return health;}
        public int getAttackPower(){return attackPower;}
        public boolean isEnemy() {return isEnemy;}
    }

    protected UNIT_TYPE unitType;
    protected int actionPoints;
    protected int actionPointsMax;
    protected boolean isAssigned;
    protected boolean isMoving;
    protected int foodConsumptionPerTurn;
    protected Hex hex;
    private Hex targetHex;
    private int maxHP;
    private int HP;
    private int health;
    private int maxHealth;
    protected Cost cookCost;


    protected Unit(UNIT_TYPE unitType, int actionPointsMax, int foodConsumptionPerTurn, int maxHP){
        this.unitType = unitType;
        this.cookCost = unitType.getCookCost();
        this.actionPointsMax = actionPointsMax;
        this.actionPoints = actionPointsMax;
        this.isAssigned = false;
        this.hex = null;
        this.targetHex = null;
        this.foodConsumptionPerTurn = foodConsumptionPerTurn;
        this.maxHP = maxHP;
        this.HP = maxHP;
        this.maxHealth = unitType.getHealth();
        this.health = maxHealth;
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
    public Cost getCookCost() { return cookCost; }
    public String getUnitImageName() {
        return unitType.toString().toLowerCase().replace("_", "");
    }
    public abstract boolean isCombatUnit();
    public int getAttackRadius(){
        return unitType.getAttackRadius();
    }
    public int getAttackPower(){
        return unitType.getAttackPower();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void resetActionPointsForNewTurn() {
        this.actionPoints = actionPointsMax;
    }

}

