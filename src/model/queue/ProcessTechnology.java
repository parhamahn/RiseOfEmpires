package model.queue;

import model.building.edge.Wall;
import model.map.Hex;
import model.state.GameState;

public class ProcessTechnology {

    public static void processDefensivePattern(GameState gameState) {
        for (Hex townHallHex : gameState.getEmpireState().getTownHallHexes()) {
            for (Hex neigh : gameState.getWorldState().getMap().getNeighbourHexes(townHallHex)) {
                if (neigh == null || townHallHex.hasWallTo(neigh)) continue;
                Wall wall = new Wall(townHallHex, neigh, gameState.getWorldState().getMap());
                townHallHex.getWallsWith().add(wall);
                neigh.getWallsWith().add(wall);
            }
            if (townHallHex.getBuilding() != null) {
                townHallHex.getBuilding().setMaxHP(350);
            }
        }
    }

}
