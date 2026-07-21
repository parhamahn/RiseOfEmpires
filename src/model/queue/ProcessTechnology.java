package model.queue;

import model.building.Wall;
import model.map.Hex;
import model.state.GameState;

public class ProcessTechnology {

    public static void processDefensivePattern(GameState gameState) {
        Hex townHallHex = gameState.getEmpireState().getTownHallHex();
        for (Hex neigh : gameState.getWorldState().getMap().getNeighbourHexes(townHallHex)) {
            Wall wall = new Wall(townHallHex, neigh, gameState.getWorldState().getMap());
            townHallHex.getWallsWith().add(wall);
            neigh.getWallsWith().add(wall);
        }
    }

}
