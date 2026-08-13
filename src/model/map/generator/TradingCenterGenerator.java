package model.map.generator;

import model.building.Building;
import model.building.other.TradingCenter;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradingCenterGenerator {
    public static int count = 5;

    public static void generateTradingCenters(GameState gameState){
        Map map = gameState.getWorldState().getMap();
        Random rand = gameState.getRng();
        List<Hex> candidates = new ArrayList<>(map.getAllHexes().stream()
                .filter(hex -> hex.getBuilding() == null)
                .filter(hex -> hex.getTerrainType() != Hex.TerrainType.SEA
                        && hex.getTerrainType() != Hex.TerrainType.TALL_MOUNTAIN)
                .toList());
        int placed = 0;
        while (placed < count && !candidates.isEmpty()) {
            Hex hex = candidates.remove(rand.nextInt(candidates.size()));
            Building building = new TradingCenter(hex);
            hex.setBuilding(building);
            gameState.getWorldState().getTradingCenters().add((TradingCenter) building);
            placed++;
        }
    }

}
