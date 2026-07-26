package model.map;

import model.building.Building;
import model.building.edge.Road;
import model.building.edge.River;
import model.building.edge.Wall;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hex {
    private static final int FLAT_AP_COST = 1;
    private static final int FOREST_AP_COST = 2;
    private static final int MOUNTAIN_AP_COST = 4;
    private static final int SEA_COST = 2;

    public enum TerrainType {PLAIN, FOREST, MOUNTAIN, MEADOW, SEA, TALL_MOUNTAIN}
    public enum ResourceType {NONE, WOOD, STONE, IRON, WHEAT, RICE, COW, SHEEP, FISH}

    private int c,r;
    private boolean discovered;
    private boolean inEmpireBorder;
    private TerrainType terrainType;
    private HashMap<ResourceType, Integer> resources;
    private Building building;
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
    public Building getBuilding() {
        return building;
    }
    public void setBuilding(Building building) {
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
            case TALL_MOUNTAIN -> Integer.MAX_VALUE;
        };
    }
}
