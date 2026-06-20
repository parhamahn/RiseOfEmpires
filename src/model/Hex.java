package model;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    public enum TerrainType {PLAIN, FOREST, MOUNTAIN, MEADOW}
    public enum ResourceType {NONE, WOOD, STONE, IRON, WHEAT, RICE, COW, SHEEP}

    private int c,r;
    private boolean discovered;
    private TerrainType terrainType;
    private List<ResourceType> resourceTypes;
    private Building building;

    public Hex(int c, int r, TerrainType terrainType){
        this.c = c;
        this.r = r;
        this.terrainType = terrainType;
        this.resourceTypes = new ArrayList<>();
        this.building = null;
    }


    public int getC() {return c;}
    public void setC(int c) {this.c = c;}

    public int getR() {return r;}
    public void setR(int r) {this.r = r;}

    public boolean isDiscovered() {return discovered;}
    public void setDiscovered(boolean discovered) {this.discovered = discovered;}

    public TerrainType getTerrainType() {return terrainType;}
    public void setTerrainType(TerrainType terrainType) {this.terrainType = terrainType;}

    public List<ResourceType> getResourceTypes() {return resourceTypes;}
    public void setResourceTypes(List<ResourceType> resourceTypes) {this.resourceTypes = resourceTypes;}

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
