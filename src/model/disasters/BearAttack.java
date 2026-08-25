package model.disasters;

import model.MapStructure;
import model.building.Building;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.Unit;
import model.unit.enemy.Bear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BearAttack implements Disaster {
    public static int UNIT_DAMAGE = 35;
    public static int BUILDING_DAMAGE = 20;
    public static int MAX_SPAWN_DISTANCE = 7;
    public static int MAX_MOVE_DISTANCE = 3;

    private final Hex center;
    private final Hex forestHex;
    private final List<Hex> involvedHexes;
    private final Unit bear;

    public BearAttack(Map map, GameState gameState, Random rand) {
        List<Hex> playerHexes = new ArrayList<>();
        for (model.state.EmpireState empireState : gameState.getEmpireStates().values()) {
            for (Unit unit : empireState.getUnits()) {
                if (unit.getHex() != null && !playerHexes.contains(unit.getHex())) {
                    playerHexes.add(unit.getHex());
                }
            }
        }
        if (playerHexes.isEmpty()) {
            for (model.state.EmpireState empireState : gameState.getEmpireStates().values()) {
                for (Building building : empireState.getBuildings()) {
                    if (building.getHex() != null && !playerHexes.contains(building.getHex())) {
                        playerHexes.add(building.getHex());
                    }
                }
            }
        }
        if (playerHexes.isEmpty()) {
            this.bear = null;
            this.center = null;
            this.forestHex = null;
            this.involvedHexes = List.of();
            return;
        }

        List<Hex> candidateForests = new ArrayList<>();
        for (Hex hex : map.getAllHexes()) {
            if (hex.getTerrainType() != Hex.TerrainType.FOREST) continue;
            if (hex.getBuilding() != null) continue;
            for (Hex playerHex : playerHexes) {
                if (map.getDistance(hex, playerHex) <= MAX_SPAWN_DISTANCE) {
                    candidateForests.add(hex);
                    break;
                }
            }
        }
        if (candidateForests.isEmpty()) {
            this.bear = null;
            this.center = null;
            this.forestHex = null;
            this.involvedHexes = List.of();
            return;
        }

        Hex nearestPlayerHex = null;
        int bestDistance = Integer.MAX_VALUE;
        List<Hex> closestForests = new ArrayList<>();
        for (Hex forest : candidateForests) {
            Hex nearest = nearestPlayerHex(forest, playerHexes, map);
            int distance = map.getDistance(forest, nearest);
            if (distance < bestDistance) {
                bestDistance = distance;
                closestForests.clear();
                closestForests.add(forest);
                nearestPlayerHex = nearest;
            } else if (distance == bestDistance) {
                closestForests.add(forest);
            }
        }

        Hex chosenForest = closestForests.get(rand.nextInt(closestForests.size()));

        this.forestHex = chosenForest;
        this.bear = new Bear();

        Hex reached = moveToward(map, chosenForest, nearestPlayerHex);
        this.center = reached;

        bear.setHex(reached);
        gameState.getWorldState().getEnemies().add(bear);

        List<Hex> area = new ArrayList<>();
        area.add(reached);
        for (Hex neighbour : map.getNeighbourHexes(reached)) {
            if (neighbour != null
                    && neighbour.getTerrainType() != Hex.TerrainType.SEA
                    && neighbour.getTerrainType() != Hex.TerrainType.TALL_MOUNTAIN
                    && !area.contains(neighbour)) {
                area.add(neighbour);
            }
        }
        this.involvedHexes = area;
    }

    private Hex nearestPlayerHex(Hex from, List<Hex> playerHexes, Map map) {
        Hex nearest = null;
        int best = Integer.MAX_VALUE;
        for (Hex hex : playerHexes) {
            int distance = map.getDistance(from, hex);
            if (distance < best) {
                best = distance;
                nearest = hex;
            }
        }
        return nearest;
    }

    private Hex moveToward(Map map, Hex from, Hex target) {
        Hex current = from;
        for (int i = 0; i < MAX_MOVE_DISTANCE; i++) {
            Hex next = null;
            int best = map.getDistance(current, target);
            for (Hex neighbour : map.getNeighbourHexes(current)) {
                if (neighbour == null) continue;
                if (neighbour.getTerrainType() == Hex.TerrainType.SEA
                        || neighbour.getTerrainType() == Hex.TerrainType.TALL_MOUNTAIN) continue;
                if (neighbour.getBuilding() != null) continue;
                int distance = map.getDistance(neighbour, target);
                if (distance < best) {
                    best = distance;
                    next = neighbour;
                }
            }
            if (next == null) break;
            current = next;
            if (map.getDistance(current, target) <= 2) break;
        }
        return current;
    }

    public void effect(GameState gameState) {
        if (bear == null || center == null) return;
        List<Unit> dead = new ArrayList<>();
        List<Building> destroyed = new ArrayList<>();

        for (Hex hex : involvedHexes) {
            for (Unit unit : hex.getUnits()) {
                if (unit.getUnitType().isEnemy()) continue;
                unit.setHP(unit.getHP() - UNIT_DAMAGE);
                unit.setActionPoints(0);
                if (unit.getHP() <= 0) {
                    dead.add(unit);
                }
            }

            MapStructure structure = hex.getBuilding();
            if (structure instanceof Building building && !building.isTownHall()) {
                building.setHP(building.getHP() - BUILDING_DAMAGE);
                if (building.getHP() <= 0) {
                    destroyed.add(building);
                }
            }
        }

        for (Unit unit : dead) {
            gameState.removeDeadUnit(unit);
        }
        for (Building building : destroyed) {
            gameState.removeDestroyedStructure(building);
        }
    }

    public Hex getCenter() {
        return center;
    }

    public Hex getForestHex() {
        return forestHex;
    }

    public List<Hex> getInvolvedHexes() {
        return involvedHexes;
    }
}
