package model.map.generator;

import model.state.GameState;

public class Generator {

    public static void generate(GameState gameState) {

        RiverGenerator.generateRivers(gameState);
        TradingCenterGenerator.generateTradingCenters(gameState);
        TribeGenerator.generateTribes(gameState);
        EnemyGenerator.generateEnemies(gameState);

    }

}
