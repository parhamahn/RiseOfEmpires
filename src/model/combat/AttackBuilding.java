package model.combat;

import model.MapStructure;
import model.building.edge.Wall;
import model.map.Hex;
import model.state.GameState;
import model.unit.Unit;


public class AttackBuilding {

    private GameState gameState;
    private final Hex attackerHex;
    private final Hex defenderHex;
    private int attackerPower;

    public AttackBuilding(GameState gameState, Hex attackerHex, Hex defenderHex){
        this.attackerHex = attackerHex;
        this.defenderHex = defenderHex;
        this.gameState = gameState;
        setAttackerPower();
    }

    public int getDistance() {
        return gameState.getWorldState().getMap().getDistance(attackerHex, defenderHex);
    }

    public boolean canTargetStructure() {
        for (Unit u : defenderHex.getUnits()) if (u.isCombatUnit()) return false;
        return true;
    }

    public void setAttackerPower(){
        int power = 0;
        for (Unit u : attackerHex.getUnits()) {
            if (u.isCombatUnit() && getDistance() <= u.getAttackRadius()) {
                power += u.getAttackPower();
            }
        }
        this.attackerPower = power;
    }

    public void attack() {
        if (!canTargetStructure()) return;

        for (Unit u : attackerHex.getUnits()) {
            if (u.isCombatUnit() && getDistance() <= u.getAttackRadius() && u.getActionPoints()>0) {
                u.setActionPoints(u.getActionPoints() - 1);
            }
        }

        for (Wall wall : defenderHex.getWallsWith()) {
            if (wall.connects(defenderHex, attackerHex)) {
                int before = wall.getHP();
                wall.setHP(before - attackerPower);
                attackerPower -= Math.min(attackerPower, before);
                if (wall.getHP() <= 0) {
                    defenderHex.getWallsWith().remove(wall);
                    attackerHex.getWallsWith().remove(wall);
                }
                break;
            }
        }

        if (attackerPower > 0 && defenderHex.getBuilding() != null) {
            MapStructure structure = defenderHex.getBuilding();
            structure.setHP(structure.getHP() - attackerPower);
            if (structure.getHP() <= 0) {
                gameState.removeDestroyedStructure(structure);
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public Hex getAttackerHex() {
        return attackerHex;
    }

    public Hex getDefenderHex() {
        return defenderHex;
    }

    public int getAttackerPower() {
        return attackerPower;
    }
}
