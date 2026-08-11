package model.map;

import model.MapStructure;
import model.Season;
import model.building.edge.Road;
import model.building.edge.River;
import model.building.edge.Wall;
import model.unit.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hex implements Serializable {
private static final long serialVersionUID = 1L;
    private static final int FLAT_AP_COST = 1;
    private static final int FOREST_AP_COST = 2;
    private static final int MOUNTAIN_AP_COST = 4;
    private static final int SEA_COST = 0;

    public enum TerrainType {PLAIN, FOREST, MOUNTAIN, MEADOW, SEA, TALL_MOUNTAIN}
    public enum ResourceType {NONE, WOOD, STONE, IRON, WHEAT, RICE, COW, SHEEP, FISH}

    private int c,r;
    private boolean discovered;
    private boolean inEmpireBorder;
    private TerrainType terrainType;
    private HashMap<ResourceType, Integer> resources;
    private MapStructure building;
    private List<Unit> units;
    private List<Road> roadsTo;
    private List<River> riversWith;
    private List<Wall> wallsWith;


    public Hex(int c, int r, TerrainType terrainType){
        this.c = c;
        this.r = r;
        this.terrainType = terrainType;
        this.resources = new HashMap<>();
        this.building = null;
        this.units = new ArrayList<>();
        this.roadsTo = new ArrayList<>();
        this.riversWith = new ArrayList<>();
        this.wallsWith = new ArrayList<>();
    }


    public boolean isInEmpireBorder() {
        return inEmpireBorder;
    }
    public void setInEmpireBorder(boolean inEmpireBorder) {
        this.inEmpireBorder = inEmpireBorder;
    }
    public int getC() {return c;}
    public int getR() {return r;}
    public boolean isDiscovered() {return discovered;}
    public void setDiscovered(boolean discovered) {this.discovered = discovered;}
    public TerrainType getTerrainType() {return terrainType;}
    public void setTerrainType(TerrainType terrainType) {this.terrainType = terrainType;}
    public HashMap<ResourceType, Integer> getResources() {return resources;}
    public MapStructure getBuilding() {
        return building;
    }
    public void setBuilding(MapStructure building) {
        this.building = building;
    }
    public List<Unit> getUnits() {
        return units;
    }

    public List<Road> getRoadsTo() { return roadsTo; }
    public List<River> getRiversWith() { return riversWith; }
    public List<Wall> getWallsWith() { return wallsWith; }

    public boolean hasRoadTo(Hex other) {
        for (Road road : roadsTo) {
            if (road.connects(this, other)) return true;
        }
        return false;
    }

    public boolean hasWallTo(Hex other) {
        for (Wall wall : wallsWith) {
            if (wall.connects(this, other)) return true;
        }
        return false;
    }

    public boolean hasRiverTo(Hex other) {
        for (River river : riversWith) {
            if (river.connects(this, other)) return true;
        }
        return false;
    }


    public int getAPToMove(){
        return switch (this.terrainType){
            case MOUNTAIN -> MOUNTAIN_AP_COST;
            case FOREST -> FOREST_AP_COST;
            case MEADOW, PLAIN -> FLAT_AP_COST;
            case SEA -> SEA_COST;
            case TALL_MOUNTAIN -> Integer.MAX_VALUE/2;
        };
    }

    public int getMoveAPCostTo(Hex target, Season.SeasonType season) {
        int APNeeded = target.getAPToMove();
        if (season == Season.SeasonType.WINTER) {
            APNeeded += 1;
        }

        for (River river : riversWith) {
            if (river.connects(this, target)) {
                APNeeded += 1;
            }
        }

        for (Road road : roadsTo) {
            if (road.connects(this, target)) {
                APNeeded = (int) Math.max(1, Math.floor(APNeeded / 2));
                break;
            }
        }

        if (season == Season.SeasonType.FALL && target.getTerrainType() == TerrainType.SEA) {
            APNeeded += 1;
        }

        return APNeeded;
    }

    public int getUnitTypeCount(Unit.UNIT_TYPE type) {
        int count = 0;
        for (Unit u : units) {
            if (u.getUnitType() == type) count++;
        }
        return count;
    }

    public boolean canAddUnitType(Unit.UNIT_TYPE type) {
        return switch (type) {
            case SWORDSMAN -> getUnitTypeCount(type) < 2;
            case ARCHER -> getUnitTypeCount(type) < 2;
            case CAVALRY -> getUnitTypeCount(type) < 1;
            case CATAPULT -> getUnitTypeCount(type) < 1;
            default -> true;
        };
    }
}
