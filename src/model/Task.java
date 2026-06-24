package model;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public enum TASK {FARM, LUMBER_MILL, STONE_MINE, IRON_MINE, TOWN, STABLE}

    public static List<TASK> getAvailableTasks(Hex hex){

        List<TASK> availableTasks = new ArrayList<>();
        if(hex.isDiscovered()) {
            switch (hex.getTerrainType()) {
                case PLAIN: {
                    if (hex.getResourceTypes().contains(Hex.ResourceType.COW) || hex.getResourceTypes().contains(Hex.ResourceType.SHEEP))
                        availableTasks.add(TASK.STABLE);
                    break;
                }
                case MEADOW: {
                    if (hex.getResourceTypes().contains(Hex.ResourceType.WHEAT) || hex.getResourceTypes().contains(Hex.ResourceType.RICE))
                        availableTasks.add(TASK.FARM);
                    break;
                }
                case FOREST: {
                    availableTasks.add(TASK.LUMBER_MILL);
                    break;
                }
                case MOUNTAIN: {
                    availableTasks.add(TASK.STONE_MINE);
                    if (hex.getResourceTypes().contains(Hex.ResourceType.IRON)) availableTasks.add(TASK.IRON_MINE);
                    break;
                }
            }
        }
        return availableTasks;
    }
}