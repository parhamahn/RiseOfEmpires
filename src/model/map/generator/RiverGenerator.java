package model.map.generator;

import model.building.edge.River;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;

import java.util.*;

public class RiverGenerator {

    public static int safeRadius = 0;

    public static void generateRivers(GameState gameState) {
        Map map = gameState.getWorldState().getMap();
        Random rng = gameState.getRng();

        int townC =  map.getCols()/ 2;
        int townR = map.getRows()/ 2;

        Set<String> usedEdges = new HashSet<>();

        int numRivers = 30 + rng.nextInt(20);

        for (int i = 0; i < numRivers; i++) {
            Hex start = findRandomRiverStart(map, townC, townR, safeRadius, rng);
            if (start == null) continue;

            List<Hex> path = new ArrayList<>();
            path.add(start);

            int length = 2 + rng.nextInt(4);
            Hex current = start;

            for (int s = 0; s < length; s++) {
                List<Hex> neighbours = map.getNeighbourHexes(current);
                List<Hex> candidates = new ArrayList<>();
                for (Hex n : neighbours) {
                    if (n == null) continue;
                    if (n.getTerrainType() == Hex.TerrainType.SEA
                            || n.getTerrainType() == Hex.TerrainType.TALL_MOUNTAIN) continue;
                    int dc = Math.abs(n.getC() - townC);
                    int dr = Math.abs(n.getR() - townR);
                    if (dc < safeRadius && dr < safeRadius) continue;
                    if (path.contains(n)) continue;
                    String edgeKey = map.edgeKey(current, n);
                    if (usedEdges.contains(edgeKey)) continue;
                    candidates.add(n);
                }
                if (candidates.isEmpty()) break;
                Hex next = candidates.get(rng.nextInt(candidates.size()));
                usedEdges.add(map.edgeKey(current, next));
                path.add(next);
                current = next;
            }

            for (int j = 0; j < path.size() - 1; j++) {
                River river = new River(path.get(j), path.get(j + 1), map);
                if (river.isValid()) {
                    path.get(j).getRiversWith().add(river);
                    path.get(j + 1).getRiversWith().add(river);
                }
            }
        }
    }

    private static Hex findRandomRiverStart(Map map, int townC, int townR, int safeRadius, Random rng) {
        for (int attempt = 0; attempt < 300; attempt++) {
            int c = rng.nextInt(map.getCols());
            int r = rng.nextInt(map.getRows());
            Hex hex = map.getHex(c, r);
            if (hex == null) continue;
            if (hex.getTerrainType() == Hex.TerrainType.SEA
                    || hex.getTerrainType() == Hex.TerrainType.TALL_MOUNTAIN) continue;
            int dc = Math.abs(c - townC);
            int dr = Math.abs(r - townR);
            if (dc < safeRadius && dr < safeRadius) continue;
            return hex;
        }
        return null;
    }

}
