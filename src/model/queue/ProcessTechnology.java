package model.queue;

import model.building.Wall;
import model.map.Hex;
import model.state.GameState;
import view.game.GamePanel;

public class ProcessTechnology {

    public static void processDefensivePattern(GameState gameState, GamePanel gamePanel) {
        Hex townHallHex = gameState.getEmpireState().getTownHallHex();
        for(Hex neigh:gameState.getWorldState().getMap().getNeighbourHexes(townHallHex)){
            Wall wall = new Wall(townHallHex, neigh, gameState.getWorldState().getMap());
            townHallHex.getWallsWith().add(wall);
            neigh.getWallsWith().add(wall);
        }
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
    }

}
