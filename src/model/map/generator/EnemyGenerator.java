package model.map.generator;

import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.enemy.Barbarian;

public class EnemyGenerator {

    public static void generateEnemies(GameState gameState){
        Map map = gameState.getWorldState().getMap();
        Barbarian testBear = new Barbarian();
        Hex townHallnei = map.getHex(map.getCols()/ 2 - 3, map.getRows() / 2);
        testBear.setHex(townHallnei);
        townHallnei.getUnits().add(testBear);
        gameState.getWorldState().getEnemies().add(testBear);
    }

}
