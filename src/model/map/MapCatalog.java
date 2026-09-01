package model.map;

public class MapCatalog {

    public static final String[] MAP_NAMES = {"Small Forest", "Small Meadow", "Small Plain"};

    private static final String[] MAP_PATHS = {
            "/utils/maps/small forest.txt",
            "/utils/maps/small meadow.txt",
            "/utils/maps/small plain.txt"
    };

    public static Map load(int index) {
        int safeIndex = (index >= 0 && index < MAP_PATHS.length) ? index : 0;
        Map map = TextMap.fromText(MAP_PATHS[safeIndex]);
        if (map == null) {
            throw new IllegalStateException("Failed to load map: " + MAP_PATHS[safeIndex]);
        }
        return map;
    }

    public static int count() {
        return MAP_PATHS.length;
    }
}