package model;

import model.building.Building;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hex {
    public enum TerrainType {PLAIN, FOREST, MOUNTAIN, MEADOW}
    public enum ResourceType {NONE, WOOD, STONE, IRON, WHEAT, RICE, COW, SHEEP}

    private int c,r;
    private boolean discovered;
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


    public int getC() {return c;}
    public void setC(int c) {this.c = c;}

    public int getR() {return r;}
    public void setR(int r) {this.r = r;}

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

    public void setResources(HashMap<ResourceType, Integer> resources) {
        this.resources = resources;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}
