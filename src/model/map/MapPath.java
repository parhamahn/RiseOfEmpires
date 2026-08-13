package model.map;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.function.Predicate;

public class MapPath {

    public static boolean isTraversable(Hex hex) {
        if (hex == null) return false;
        return hex.getTerrainType() != Hex.TerrainType.SEA
                && hex.getTerrainType() != Hex.TerrainType.TALL_MOUNTAIN;
    }

    public static boolean isPlayerClear(Hex hex) {
        return hex == null || hex.getUnits().stream().noneMatch(unit -> !unit.getUnitType().isEnemy());
    }

    public static boolean isPassableForEnemy(Hex hex) {
        return isTraversable(hex) && isPlayerClear(hex);
    }

    public static Hex nextStepToward(Map map, Hex from, Hex to, Predicate<Hex> passable) {
        if (from == null || to == null || from == to) return null;
        java.util.Map<Hex, Hex> cameFrom = new HashMap<>();
        Deque<Hex> queue = new ArrayDeque<>();
        queue.add(to);
        cameFrom.put(to, null);
        while (!queue.isEmpty()) {
            Hex current = queue.poll();
            if (current == from) break;
            for (Hex neighbour : map.getNeighbourHexes(current)) {
                if (neighbour == null || cameFrom.containsKey(neighbour)) continue;
                if (!passable.test(neighbour)) continue;
                cameFrom.put(neighbour, current);
                queue.add(neighbour);
            }
        }
        return cameFrom.get(from);
    }
}
