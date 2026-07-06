package model;

import model.building.Building;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hex {
    private static final int FLAT_AP_COST = 1;
    private static final int FOREST_AP_COST = 2;
    private static final int MOUNTAIN_AP_COST = 4;

    public enum TerrainType {PLAIN, FOREST, MOUNTAIN, MEADOW}
    public enum ResourceType {NONE, WOOD, STONE, IRON, WHEAT, RICE, COW, SHEEP}

    private int c,r;
    private boolean discovered;
    private boolean inEmpireBorder;
    private TerrainType terrainType;
    private HashMap<ResourceType, Integer> resources;
    private Building building;
    private List<Unit> units;


    public Hex(int c, int r, TerrainType terrainType){
        this.c = c;
        this.r = r;
        this.terrainType = terrainType;
        this.resources = new HashMap<>();
        this.building = null;
        this.units = new ArrayList<>();
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


    public int getAPToMove(){
        return switch (this.terrainType){
            case MOUNTAIN -> MOUNTAIN_AP_COST;
            case FOREST -> FOREST_AP_COST;
            case MEADOW, PLAIN -> FLAT_AP_COST;
        };
    }
}
