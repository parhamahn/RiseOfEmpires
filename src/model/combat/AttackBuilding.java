package model.combat;

import model.building.Building;
import model.building.edge.Wall;
import model.building.tribes.*;
import model.map.Hex;
import model.state.GameState;
import model.storage.Storage;
import model.unit.Unit;
import model.unit.enemy.Guard;

import java.util.ArrayList;
import java.util.List;


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
            Building building = defenderHex.getBuilding();
            building.setHP(building.getHP() - attackerPower);
            if (building.getHP() <= 0) {
                if (building instanceof Tribe) {
                    destroyCamp((Tribe) building);
                } else {
                    gameState.getEmpireState().removeBuilding(building);
                }
            }
        }
    }

    private void destroyCamp(Tribe tribe) {
        Hex campHex = tribe.getHex();
        gameState.getWorldState().getTribes().remove(tribe);
        gameState.getEmpireState().getAllies().remove(tribe);
        List<Unit> guards = new ArrayList<>(campHex.getUnits());
        for (Unit unit : guards) {
            if (unit instanceof Guard) {
                campHex.getUnits().remove(unit);
                gameState.getWorldState().getEnemies().remove(unit);
            }
        }
        campHex.setBuilding(null);
        campHex.setInEmpireBorder(true);
        for (Hex neighbour : gameState.getWorldState().getMap().getNeighbourHexes(campHex)) {
            if (neighbour != null
                    && neighbour.getTerrainType() != Hex.TerrainType.SEA
                    && neighbour.getTerrainType() != Hex.TerrainType.TALL_MOUNTAIN
                    && !neighbour.isInEmpireBorder()) {
                neighbour.setInEmpireBorder(true);
            }
        }
        collectLoot(tribe);
    }

    private void collectLoot(Tribe tribe) {
        Storage storage = gameState.getEmpireState().getStorage();
        if (tribe instanceof FarmerTribe) {
            storage.setFoodQuantity(storage.getFoodQuantity() + 40);
        } else if (tribe instanceof MountainTribe) {
            storage.setStoneQuantity(storage.getStoneQuantity() + 30);
            storage.setIronQuantity(storage.getIronQuantity() + 20);
        } else if (tribe instanceof BeachTribe) {
            storage.setFoodQuantity(storage.getFoodQuantity() + 30);
            storage.setWoodQuantity(storage.getWoodQuantity() + 20);
        } else if (tribe instanceof TraderTribe) {
            storage.setFoodQuantity(storage.getFoodQuantity() + 30);
            storage.setWoodQuantity(storage.getWoodQuantity() + 30);
        } else if (tribe instanceof WarriorTribe) {
            storage.setIronQuantity(storage.getIronQuantity() + 30);
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
