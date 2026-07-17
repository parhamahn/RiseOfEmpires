package model;

import model.building.Bazaar;
import model.building.MilitaryStable;
import model.map.Hex;
import model.state.GameState;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public enum TASK {
        FARM,
        LUMBER_MILL,
        STONE_MINE,
        IRON_MINE,
        TOWN,
        STABLE,
        MONUMENT,
        BAZAAR,
        DOCK,
        MILITARY_STABLE,
        WALL,
    }

    public static List<TASK> getAvailableTasks(Hex hex, GameState gameState) {
        List<TASK> availableTasks = new ArrayList<>();
        if (hex.isDiscovered() && hex.getBuilding()==null) {
            availableTasks.add(TASK.TOWN);
            availableTasks.add(TASK.WALL);
            switch (hex.getTerrainType()) {
                case PLAIN: {
                    if ((hex.getResources().getOrDefault(Hex.ResourceType.COW,   0) > 0 || hex.getResources().getOrDefault(Hex.ResourceType.SHEEP,  0) > 0)) {availableTasks.add(TASK.STABLE);}
                    availableTasks.add(TASK.MONUMENT);
                    if(gameState.getEmpireState().getTownHallLevel().ordinal()>=Bazaar.getLevel().ordinal()){availableTasks.add(TASK.BAZAAR);}
                    if(gameState.getEmpireState().getTownHallLevel().ordinal()>= MilitaryStable.getLevel().ordinal()){availableTasks.add(TASK.MILITARY_STABLE);}
                    break;
                }
                case MEADOW: {
                    if (hex.getResources().getOrDefault(Hex.ResourceType.WHEAT, 0) > 0 || hex.getResources().getOrDefault(Hex.ResourceType.RICE,  0) > 0) {
                        availableTasks.add(TASK.FARM);
                    }
                    if(gameState.getEmpireState().getTownHallLevel().ordinal()>=Bazaar.getLevel().ordinal()){availableTasks.add(TASK.BAZAAR);}
                    break;
                }
                case FOREST: {
                    if (hex.getResources().getOrDefault(Hex.ResourceType.WOOD,  0) > 0) {
                        availableTasks.add(TASK.LUMBER_MILL);
                    }
                    if(gameState.getEmpireState().getTownHallLevel().ordinal()>=Bazaar.getLevel().ordinal()){availableTasks.add(TASK.BAZAAR);}
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
            for(Hex neighhex:gameState.getWorldState().getMap().getNeighbourHexes(hex)){
                if(neighhex.isDiscovered() && neighhex.getTerrainType()== Hex.TerrainType.SEA){availableTasks.add(TASK.DOCK);}
            }
        }
        return availableTasks;
    }
}