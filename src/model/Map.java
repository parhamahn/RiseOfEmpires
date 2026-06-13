package model;

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
                grid[r][c].setDiscovered(true);
            }
        }

        Hex townHallHex = getHex(cols / 2, rows / 2);
        townHallHex.setDiscovered(true);
        int[][] neighbours = {
                { 1,  0},
                {-1,  0},
                { 1,  1},
                { 0,  1},
                { 1, -1},
                { 0, -1}
        };
        boolean jungleInNeighbour = false;
        for(int[] neighbour:neighbours){
            Hex neigh = getHex(townHallHex.getC()+neighbour[0], townHallHex.getR()+neighbour[1]);
            neigh.setDiscovered(true);
            if (neigh.getTerrainType()== Hex.TerrainType.FOREST){
                jungleInNeighbour = true;
            }
        }

        if(!jungleInNeighbour){
            getHex(townHallHex.getC()+neighbours[3][0], townHallHex.getR()+neighbours[3][1]).setTerrainType(Hex.TerrainType.FOREST);
        }

    }

    public Hex getHex(int x, int y) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            return grid[y][x];
        }
        return null;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
