package model.building;

import model.Task;
import model.building.edge.Road;
import model.building.edge.Wall;
import model.building.empire.*;
import model.map.Hex;
import model.storage.Cost;

public class BuildingFactory {

    public static Cost getBuildCost(Task.TASK task) {
        return switch (task) {
            case FARM        -> Farm.getBuildCost();
            case LUMBER_MILL -> LumberMill.getBuildCost();
            case STONE_MINE  -> StoneMine.getBuildCost();
            case IRON_MINE   -> IronMine.getBuildCost();
            case STABLE      -> Stable.getBuildCost();
            case TOWN        -> Town.getBuildCost();
            case MONUMENT    -> Monument.getBuildCost();
            case BAZAAR      -> Bazaar.getBuildCost();
            case DOCK        -> Dock.getBuildCost();
            case MILITARY_STABLE -> MilitaryStable.getBuildCost();
            case WALL        -> Wall.getBuildCost();
            case ROAD        -> Road.getBuildCost();
        };
    }

    public static Building createBuilding(Task.TASK task, Hex hex) {
        return switch (task) {
            case FARM -> new Farm(hex, hex.getResources().containsKey(Hex.ResourceType.WHEAT) ? Hex.ResourceType.WHEAT : Hex.ResourceType.RICE);
            case LUMBER_MILL -> new LumberMill(hex);
            case STONE_MINE -> new StoneMine(hex);
            case IRON_MINE -> new IronMine(hex);
            case STABLE -> new Stable(hex, Hex.ResourceType.COW);
            case TOWN -> new Town(hex);
            case MONUMENT -> new Monument(hex);
            case BAZAAR -> new Bazaar(hex);
            case DOCK -> new Dock(hex);
            case MILITARY_STABLE -> new MilitaryStable(hex);
            case WALL, ROAD -> null;
        };
    }
}
