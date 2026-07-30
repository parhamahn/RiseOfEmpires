package model.map.generator;

import model.state.GameState;

public class Generator {

    public static void generate(GameState gameState) {

        EnemyGenerator.generateEnemies(gameState);
        RiverGenerator.generateRivers(gameState);
        TradingCenterGenerator.generateTradingCenters(gameState);
        TribeGenerator.generateTribes(gameState);

    }

}
