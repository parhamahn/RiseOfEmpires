package model;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public enum TASK {FARM, LUMBER_MILL, STONE_MINE, IRON_MINE, TOWN, STABLE}

    public static List<TASK> getAvailableTasks(Hex hex) {
        List<TASK> availableTasks = new ArrayList<>();
        if (hex.isDiscovered()) {
            availableTasks.add(TASK.TOWN);
            switch (hex.getTerrainType()) {
                case PLAIN: {
                    if (hex.getResources().getOrDefault(Hex.ResourceType.COW,   0) > 0
                            || hex.getResources().getOrDefault(Hex.ResourceType.SHEEP,  0) > 0)
                        availableTasks.add(TASK.STABLE);
                    break;
                }
                case MEADOW: {
                    if (hex.getResources().getOrDefault(Hex.ResourceType.WHEAT, 0) > 0
                            || hex.getResources().getOrDefault(Hex.ResourceType.RICE,  0) > 0)
                        availableTasks.add(TASK.FARM);
                    break;
                }
                case FOREST: {
                    if (hex.getResources().getOrDefault(Hex.ResourceType.WOOD,  0) > 0)
                        availableTasks.add(TASK.LUMBER_MILL);
                    break;
                }
                case MOUNTAIN: {
                    if (hex.getResources().getOrDefault(Hex.ResourceType.STONE, 0) > 0)
                        availableTasks.add(TASK.STONE_MINE);
                    if (hex.getResources().getOrDefault(Hex.ResourceType.IRON,  0) > 0)
                        availableTasks.add(TASK.IRON_MINE);
                    break;
                }
            }
        }
        return availableTasks;
    }

    public static Cost getCostForTask(TASK task) {
        return switch (task) {
            case FARM        -> new Cost(5,  0,  0, 2);
            case LUMBER_MILL -> new Cost(5,  0,  0, 2);
            case STONE_MINE  -> new Cost(25, 0,  0, 3);
            case IRON_MINE   -> new Cost(40, 0,  0, 3);
            case STABLE      -> new Cost(20, 0,  0, 2);
            case TOWN        -> new Cost(40, 20, 10, 3);
        };
    }

}