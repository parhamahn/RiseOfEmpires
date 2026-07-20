package model.combat;

import model.building.Building;
import model.building.Wall;
import model.map.Hex;
import model.map.Map;
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
        Map map = gameState.getWorldState().getMap();
        if (map.getNeighbourHexes(attackerHex).contains(defenderHex)) return 1;
        for (Hex n : map.getNeighbourHexes(attackerHex)) {
            if (map.getNeighbourHexes(n).contains(defenderHex)) return 2;
        }
        return -1;
    }

    public void setAttackerPower(){
        int power = 0;
        for(Unit unit:attackerHex.getUnits()){
            if(unit.isCombatUnit() && getDistance()<=unit.getAttackRadius()){
                power+=unit.getAttackPower();
            }
        }
        this.attackerPower = power;
    }

    public void attack(){
        for(Wall wall:defenderHex.getWallsWith()){
            if(wall.connects(defenderHex, attackerHex)){
                wall.setHP(wall.getHP()-attackerPower);
                attackerPower-=Math.max(0,attackerPower-wall.getHP());
                if(wall.getHP()<=0){
                    defenderHex.getUnits().remove(wall);
                }
                break;
            }
        }
        if(attackerPower != 0){
            Building building = defenderHex.getBuilding();
            building.setHP(building.getHP()-attackerPower);
            if(building.getHP()<=0){
                defenderHex.setBuilding(null);
                gameState.getEmpireState().getBuildings().remove(building);
                for(Unit unit:defenderHex.getUnits()){
                    unit.setAssigned(false);
                }
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
