package model;

import model.building.Building;
import model.building.empire.Apothecary;
import model.building.empire.Bazaar;
import model.building.empire.Dock;
import model.building.empire.MilitaryStable;
import model.map.Hex;
import model.state.GameState;
import model.storage.Cost;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        ROAD,
        APOTHECARY
    }

    public static List<TASK> getAvailableTasks(Hex hex, GameState gameState) {
        return getAvailableTasks(hex, gameState, gameState.resolveActivePlayerId());
    }

    public static List<TASK> getAvailableTasks(Hex hex, GameState gameState, java.util.UUID playerId) {
        List<TASK> availableTasks = new ArrayList<>();
        if(hex.getTerrainType()== Hex.TerrainType.TALL_MOUNTAIN || hex.getTerrainType()== Hex.TerrainType.SEA) {return availableTasks;}
        if (hex.isDiscoveredBy(playerId) && hex.getBuilding()==null) {
            availableTasks.add(TASK.TOWN);
            switch (hex.getTerrainType()) {
                case PLAIN: {
                    if ((hex.getResources().getOrDefault(Hex.ResourceType.COW,   0) > 0 || hex.getResources().getOrDefault(Hex.ResourceType.SHEEP,  0) > 0)) {availableTasks.add(TASK.STABLE);}
                    availableTasks.add(TASK.MONUMENT);
                    if(gameState.getEmpireState().getTownHallLevel().ordinal()>=Bazaar.getLevel().ordinal()){availableTasks.add(TASK.BAZAAR);}
                    if(gameState.getEmpireState().getTownHallLevel().ordinal()>= MilitaryStable.getLevel().ordinal()){availableTasks.add(TASK.MILITARY_STABLE);}
                    if(gameState.getEmpireState().getTownHallLevel().ordinal()>= Apothecary.getLevel().ordinal()){availableTasks.add(TASK.APOTHECARY);}
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
            if(gameState.getEmpireState().getTownHallLevel().ordinal() >= Dock.getLevel().ordinal()
                    && gameState.getWorldState().getMap().getNeighbourHexes(hex).stream()
                    .filter(Objects::nonNull)
                    .anyMatch(h -> h.getTerrainType() == Hex.TerrainType.SEA && h.isDiscoveredBy(playerId) && h.getResources().containsKey(Hex.ResourceType.FISH))){
                availableTasks.add(TASK.DOCK);
            }
        }
        if (hex.isDiscoveredBy(playerId)) {
            availableTasks.add(TASK.WALL);
            availableTasks.add(TASK.ROAD);
        }
        return availableTasks;
    }

    public static boolean canBuild(TASK task, Hex hex, GameState gameState) {
        Cost baseCost = Building.getBuildCost(task);
        Cost buildCost = task == TASK.DOCK && gameState.getEmpireState().isDockDiscount()
                ? new Cost(baseCost.getWoodNeeded()/2, baseCost.getStoneNeeded()/2, baseCost.getIronNeeded()/2, baseCost.getFoodNeeded()/2, baseCost.getAPNeeded())
                : baseCost;
        boolean enoughAP = hex.getUnits().stream()
                .anyMatch(unit -> unit.isBuilder() && buildCost.enoughAP(unit.getActionPoints()));
        return buildCost.enoughResources(gameState.getEmpireState().getStorage())
                && enoughAP
                && hex.isOwnedBy(gameState.resolveActivePlayerId());
    }

    public static String getBuildBlockReason(TASK task, Hex hex, GameState gameState) {
        if (canBuild(task, hex, gameState)) return null;
        Cost baseCost = Building.getBuildCost(task);
        Cost buildCost = task == TASK.DOCK && gameState.getEmpireState().isDockDiscount()
                ? new Cost(baseCost.getWoodNeeded()/2, baseCost.getStoneNeeded()/2, baseCost.getIronNeeded()/2, baseCost.getFoodNeeded()/2, baseCost.getAPNeeded())
                : baseCost;
        if (!buildCost.enoughResources(gameState.getEmpireState().getStorage()))
            return "Not enough " + buildCost.missingResources(gameState.getEmpireState().getStorage());
        if (!hex.isOwnedBy(gameState.resolveActivePlayerId())) return "Hex is not within your empire border";
        return "No builder in this hex has enough action points";
    }
}