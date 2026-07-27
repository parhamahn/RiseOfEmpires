package model.combat;

import model.building.edge.Wall;
import model.map.Hex;
import model.state.GameState;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AttackUnit {
    private static final int WALL_BONUS = 2;
    private static final int MAX_DICE = 6;

    private GameState gameState;
    private final Hex attackerHex;
    private final Hex defenderHex;
    private List<Integer> attackerDices;
    private List<Integer> defenderDices;
    private int attackerPower;
    private int defenderPower;

    public AttackUnit(GameState gameState, Hex attackerHex, Hex defenderHex){
        this.attackerHex = attackerHex;
        this.defenderHex = defenderHex;
        this.gameState = gameState;
    }

    public Hex getAttackerHex() {return attackerHex;}
    public Hex getDefenderHex() {return defenderHex;}

    private boolean hasCombatType(Hex hex, Unit.UNIT_TYPE type) {
        return hex.getUnits().stream().anyMatch(u -> u.getUnitType() == type && u.isCombatUnit());
    }

    private int countCombatTypes(Hex hex) {
        int count = 0;
        for (Unit.UNIT_TYPE type : Unit.UNIT_TYPE.values()) {
            if (type == Unit.UNIT_TYPE.CATAPULT) continue;
            if (hasCombatType(hex, type)) count++;
        }
        return count;
    }

    private int getDefenderDiceCount(Hex hex) {
        boolean hasBarbarian = hasCombatType(hex, Unit.UNIT_TYPE.BARBARIAN);
        boolean hasBear = hasCombatType(hex, Unit.UNIT_TYPE.BEAR);
        if (hasBarbarian) return 2;
        if (hasBear) return 1;
        return countCombatTypes(hex);
    }

    private boolean isWalledBetween() {
        for (Wall wall : defenderHex.getWallsWith()) {
            if (wall.connects(attackerHex, defenderHex)) return true;
        }
        return false;
    }

    public boolean isLegal() {
        int d = getDistance();
        if (d == 1) return true;
        if (d == 2) return hasCombatType(attackerHex, Unit.UNIT_TYPE.ARCHER);
        return false;
    }

    private List<Integer> rollSortedDesc(int n) {
        List<Integer> dice = new ArrayList<>();
        for (int i = 0; i < n; i++) dice.add(Dice.getInstance().roll());
        dice.sort(Collections.reverseOrder());
        return dice;
    }

    public void rollDice() {
        int attackerCount = getDistance() == 2 ? 1 : countCombatTypes(attackerHex);
        int defenderCount = getDefenderDiceCount(defenderHex);
        attackerDices = rollSortedDesc(attackerCount);
        defenderDices = rollSortedDesc(defenderCount);

        if (isWalledBetween()) {
            for (int i = 0; i < defenderDices.size(); i++) {
                defenderDices.set(i, Math.min(MAX_DICE, defenderDices.get(i) + WALL_BONUS));
            }
        }
    }


    public int getDistance() {
        return gameState.getWorldState().getMap().getDistance(attackerHex, defenderHex);
    }


    public void setPowers() {
        attackerPower = 0;
        defenderPower = 0;
        int pairs = Math.min(attackerDices.size(), defenderDices.size());
        for (int i = 0; i < pairs; i++) {
            if (attackerDices.get(i) > defenderDices.get(i)) attackerPower++;
            else defenderPower++;
        }
    }

    public void attack() {
        for (Unit u : attackerHex.getUnits()) {
            if (u.isCombatUnit()) u.setActionPoints(u.getActionPoints() - 1);
        }
        applyDamage(defenderHex, attackerPower);
        applyDamage(attackerHex, defenderPower);
    }

    private void applyDamage(Hex hex, int damage) {
        if (damage <= 0) return;
        List<Unit> combatUnits = new ArrayList<>();
        for (Unit u : hex.getUnits()) if (u.isCombatUnit()) combatUnits.add(u);
        DamageHandler chain = new SwordsmanDamageHandler();
        chain.setNext(new ArcherDamageHandler())
                .setNext(new CavalryDamageHandler())
                .setNext(new CatapultDamageHandler())
                .setNext(new EnemyDamageHandler());
        chain.handle(combatUnits, damage, gameState);
    }

    public int getDefenderPower() {
        return defenderPower;
    }

    public int getAttackerPower() {
        return attackerPower;
    }

    public List<Integer> getDefenderDices() {
        return defenderDices;
    }

    public List<Integer> getAttackerDices() {
        return attackerDices;
    }
}
