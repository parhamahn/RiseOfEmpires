package model;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private final Hex[][] grid;
    private final int rows;
    private final int cols;

    public Map(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Hex[rows][cols];
        generateMap();
    }

    private void generateMap() {
        Hex.TerrainType[] terrains = Hex.TerrainType.values();

        int numSeeds = (rows * cols)/2;

        int[][] seedPos = new int[numSeeds][2];
        Hex.TerrainType[] seedTerrain = new Hex.TerrainType[numSeeds];

        for (int i = 0; i < numSeeds; i++) {
            seedPos[i][0]  = (int)(Math.random() * cols);
            seedPos[i][1]  = (int)(Math.random() * rows);
            seedTerrain[i] = terrains[(int)(Math.random() * terrains.length)];
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                int    nearest = 0;
                double minDist = Double.MAX_VALUE;

                for (int i = 0; i < numSeeds; i++) {
                    double dx   = c - seedPos[i][0];
                    double dy   = r - seedPos[i][1];
                    double dist = dx * dx + dy * dy;
                    if (dist < minDist) {
                        minDist  = dist;
                        nearest  = i;
                    }
                }

                grid[r][c] = new Hex(c, r, seedTerrain[nearest]);
                grid[r][c].setDiscovered(false);
            }
        }

        Hex townHallHex = getHex(cols / 2, rows / 2);
        townHallHex.setDiscovered(true);
        boolean jungleInNeighbour = false;
        for(Hex neighbour:getNeighbourHexes(townHallHex)){
            neighbour.setDiscovered(true);
            if (neighbour.getTerrainType()== Hex.TerrainType.FOREST){
                jungleInNeighbour = true;
            }
        }

        if(!jungleInNeighbour){
            getHex(townHallHex.getC(), townHallHex.getR()+1).setTerrainType(Hex.TerrainType.FOREST);
        }

    }

    public Hex getHex(int x, int y) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            return grid[y][x];
        }
        return null;
    }

    public List<Hex> getNeighbourHexes(Hex hex){
        List<Hex> neighboursTiles = new ArrayList<>();
        int[][] neighbours = {
                { 1,  0},
                {-1,  0},
                { 1,  1},
                { 0,  1},
                { 1, -1},
                { 0, -1}
        };
        for(int[] neighbour:neighbours){
            Hex neigh = getHex(hex.getC()+neighbour[0], hex.getR()+neighbour[1]);
            neighboursTiles.add(neigh);
        }
        return neighboursTiles;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
