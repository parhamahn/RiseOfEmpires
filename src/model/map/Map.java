package model.map;

import java.io.Serializable;
import java.util.*;

public class Map implements Serializable {
private static final long serialVersionUID = 1L;
    private Hex[][] grid;
    private List<Hex> spawnPoints;
    private final int rows;
    private final int cols;
    private Random rng;

    public Map(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.grid = new Hex[rows][cols];
    }

    public Map(int rows, int cols, Random rng) {
        this.rows = rows;
        this.cols = cols;
        this.rng = rng;
        this.grid = new Hex[rows][cols];
        generateMap();
        setResources();
    }

    public List<Hex> getSpawnPoints() {
        return spawnPoints;
    }

    public void setSpawnPoints(List<Hex> spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public void setRng(Random rng) {
        this.rng = rng;
    }

    public void setResources(Random rng){
        this.rng = rng;
        setResources();
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public void setHex(int r, int c, Hex hex){
        grid[r][c]=hex;
    }

    private void generateMap() {
        Hex.TerrainType[] terrains = Hex.TerrainType.values();

        int numSeeds = (rows * cols)/2;

        int[][] seedPos = new int[numSeeds][2];
        Hex.TerrainType[] seedTerrain = new Hex.TerrainType[numSeeds];

        for (int i = 0; i < numSeeds; i++) {
            seedPos[i][0]  = rng.nextInt(cols);
            seedPos[i][1]  = rng.nextInt(rows);
            seedTerrain[i] = terrains[rng.nextInt(terrains.length)];
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
        townHallHex.setTerrainType(Hex.TerrainType.PLAIN);
        townHallHex.setDiscovered(true);
        townHallHex.setInEmpireBorder(true);
        boolean jungleInNeighbour = false;
        for(Hex neighbour:getNeighbourHexes(townHallHex)){
            neighbour.setDiscovered(true);
            neighbour.setInEmpireBorder(true);
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

    public void setResources(){
        for (int i=0; i<cols; i++){
            for (int j=0; j<rows;j++){
                Hex hex = getHex(i, j);
                if (hex.getBuilding()==null){
                    HashMap<Hex.ResourceType, Integer> resources = hex.getResources();

                    if(hex.getTerrainType()== Hex.TerrainType.FOREST){

                        resources.put(Hex.ResourceType.WOOD, 200);

                    }else if (hex.getTerrainType()== Hex.TerrainType.MEADOW){

                        if (rng.nextDouble()<=0.5){
                            resources.put(Hex.ResourceType.WHEAT, 100);
                        }else{
                            if (rng.nextDouble()<=0.3) resources.put(Hex.ResourceType.RICE, 100);
                        }


                    }else if (hex.getTerrainType()== Hex.TerrainType.MOUNTAIN){

                        resources.put(Hex.ResourceType.STONE, 100);
                        if (rng.nextDouble()<=0.3) resources.put(Hex.ResourceType.IRON, 50);

                    }else if (hex.getTerrainType()== Hex.TerrainType.PLAIN){

                        if (rng.nextDouble()<=0.3) {
                            resources.put(Hex.ResourceType.COW, 200);
                        }else{
                            if (rng.nextDouble()<=0.3) resources.put(Hex.ResourceType.SHEEP, 200);
                        }
                    } else if (hex.getTerrainType()== Hex.TerrainType.SEA) {
                        if(rng.nextDouble()<=0.35){
                            resources.put(Hex.ResourceType.FISH, 100);
                        }
                    }
                }
            }
        }
    }

    public String edgeKey(Hex a, Hex b) {
        int minC = Math.min(a.getC(), b.getC());
        int maxC = Math.max(a.getC(), b.getC());
        int minR = Math.min(a.getR(), b.getR());
        int maxR = Math.max(a.getR(), b.getR());
        return minC + "," + minR + "-" + maxC + "," + maxR;
    }

    public List<Hex> getNeighbourHexes(Hex hex){
        List<Hex> neighboursTiles = new ArrayList<>();
        int[][] neighbours;
        if(hex.getR()%2==1){
            neighbours = new int[][]{
                    {1, 0},
                    {-1, 0},
                    {1, 1},
                    {0, 1},
                    {1, -1},
                    {0, -1}
            };
        }else{
            neighbours = new int[][]{
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {-1, 1},
                    {0, -1},
                    {-1, -1}
            };
        }
        for(int[] neighbour:neighbours){
            Hex neigh = getHex(hex.getC()+neighbour[0], hex.getR()+neighbour[1]);
            neighboursTiles.add(neigh);
        }
        return neighboursTiles;
    }

    public List<Hex> getAllHexes(){
        List<Hex> allHexes = new ArrayList<>();

        for(int x=0; x<cols; x++){
            for(int y=0; y<rows; y++){
                Hex hex = getHex(x, y);
                allHexes.add(hex);
            }
        }

        return  allHexes;
    }

    public int getDistance(Hex a, Hex b) {
        int[] ac = toCube(a.getC(), a.getR());
        int[] bc = toCube(b.getC(), b.getR());
        return (Math.abs(ac[0] - bc[0]) + Math.abs(ac[1] - bc[1]) + Math.abs(ac[2] - bc[2])) / 2;
    }

    private static int[] toCube(int col, int row) {
        int x = col - (row - (row & 1)) / 2;
        int z = row;
        int y = -x - z;
        return new int[]{x, y, z};
    }
}
