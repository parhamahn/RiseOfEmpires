package model.map.generator;

import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.Unit;
import model.unit.enemy.Barbarian;
import model.unit.enemy.Bear;

import java.util.Random;

public class EnemyGenerator {

    private static final int ENEMY_COUNT = 8;

    public static void generateEnemies(GameState gameState){
        Map map = gameState.getWorldState().getMap();
        Random rand = gameState.getRng();

        int placed = 0;
        int attempts = 0;
        int maxAttempts = ENEMY_COUNT * 300;
        while (placed < ENEMY_COUNT && attempts < maxAttempts) {
            attempts++;
            Hex hex = map.getHex(rand.nextInt(map.getCols()), rand.nextInt(map.getRows()));
            if (!isValidSpawnHex(map, hex)) continue;

            Unit enemy = rand.nextBoolean() ? new Barbarian() : new Bear();
            enemy.setHex(hex);
            hex.getUnits().add(enemy);
            gameState.getWorldState().getEnemies().add(enemy);
            placed++;
        }
    }

    private static boolean isValidSpawnHex(Map map, Hex hex) {
        if (hex == null) return false;
        if (hex.isInEmpireBorder()) return false;
        if (hex.getBuilding() != null || !hex.getUnits().isEmpty()) return false;
        if (hex.getTerrainType() == Hex.TerrainType.SEA || hex.getTerrainType() == Hex.TerrainType.TALL_MOUNTAIN) return false;

        for (Hex neighbour : map.getNeighbourHexes(hex)) {
            if (neighbour != null && neighbour.isInEmpireBorder()) return false;
        }
        return true;
    }

}
