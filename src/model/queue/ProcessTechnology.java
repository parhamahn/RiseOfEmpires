package model.queue;

import model.building.Wall;
import model.map.Hex;
import model.state.GameState;
import view.game.GamePanel;

public class ProcessTechnology {

    public static void processDefensivePattern(GameState gameState, GamePanel gamePanel) {

        for(Hex neigh:gameState.getWorldState().getMap().getNeighbourHexes(gameState.getEmpireState().getTownHallHex())){
            gameState.getEmpireState().getWalls().add(new Wall(gameState.getEmpireState().getTownHallHex(), neigh, gameState.getWorldState().getMap()));
        }
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();

    }

}
