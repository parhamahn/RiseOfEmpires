package model;

import model.map.Hex;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public enum TASK {
        FARM,
        LUMBER_MILL,
        STONE_MINE,
        IRON_MINE,
        TOWN,
        STABLE
    }

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
}