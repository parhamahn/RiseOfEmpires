package model.map;

import model.building.*;

import java.util.*;
import java.util.stream.Stream;

public class Map {
    private final Hex[][] grid;
    private final int rows;
    private final int cols;
    private final Random RANDOM = new Random();

    public Map(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Hex[rows][cols];
        generateMap();
        setResources();
        generateRivers();
        generateTribes();
    }


    public int getRows() { return rows; }
    public int getCols() { return cols; }


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

    public void generateTribes(){
        int farmerTribeCount = 3;
        int traderTribeCount = 3;
        int warriorTribeCount = 3;
        int mountainTribeCount = 3;
        int beachTribeCount = 3;


        List<Hex> plainHexes = new ArrayList<>();
        List<Hex> meadowHexes = new ArrayList<>();
        List<Hex> mountainHexes = new ArrayList<>();
        List<Hex> beachHexes = new ArrayList<>();
        for(Hex hex:getAllHexes()){
            switch (hex.getTerrainType()){
                case PLAIN -> plainHexes.add(hex);
                case MOUNTAIN -> mountainHexes.add(hex);
                case MEADOW -> meadowHexes.add(hex);
            }
            for(Hex neighbour:getNeighbourHexes(hex)){
                if(neighbour!=null&&neighbour.getTerrainType()== Hex.TerrainType.SEA){
                    beachHexes.add(neighbour);
                }
            }
        }
        List<Hex> plainAndMeadowHexes = Stream.concat(plainHexes.stream(), meadowHexes.stream()).toList();;
        Random rand = new Random();
        for(int i=0;i<farmerTribeCount;i++){
            Hex hex = plainAndMeadowHexes.get(rand.nextInt(plainAndMeadowHexes.size()));
            if(hex.getBuilding()!=null){
                i-=1;
            }else{
                hex.setBuilding(new FarmerTribe(hex));
            }
        }
        for(int i=0;i<mountainTribeCount;i++){
            Hex hex = mountainHexes.get(rand.nextInt(mountainHexes.size()));
            if(hex.getBuilding()!=null){
                i-=1;
            }else{
                hex.setBuilding(new MountainTribe(hex));
            }
        }
        for(int i=0;i<beachTribeCount;i++){
            Hex hex = beachHexes.get(rand.nextInt(beachHexes.size()));
            if(hex.getBuilding()!=null){
                i-=1;
            }else{
                hex.setBuilding(new BeachTribe(hex));
            }
        }
        for(int i=0;i<warriorTribeCount;i++){
            Hex hex = getAllHexes().get(rand.nextInt(getAllHexes().size()));
            if(hex.getBuilding()!=null){
                i-=1;
            }else{
                hex.setBuilding(new WarriorTribe(hex));
            }
        }
        for(int i=0;i<traderTribeCount;i++){
            Hex hex = getAllHexes().get(rand.nextInt(getAllHexes().size()));
            if(hex.getBuilding()!=null){
                i-=1;
            }else{
                hex.setBuilding(new TraderTribe(hex));
            }
        }

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

                        if (RANDOM.nextDouble()<=0.5){
                            resources.put(Hex.ResourceType.WHEAT, 100);
                        }else{
                            if (RANDOM.nextDouble()<=0.3) resources.put(Hex.ResourceType.RICE, 100);
                        }


                    }else if (hex.getTerrainType()== Hex.TerrainType.MOUNTAIN){

                        resources.put(Hex.ResourceType.STONE, 100);
                        if (RANDOM.nextDouble()<=0.3) resources.put(Hex.ResourceType.IRON, 50);

                    }else if (hex.getTerrainType()== Hex.TerrainType.PLAIN){

                        if (RANDOM.nextDouble()<=0.3) {
                            resources.put(Hex.ResourceType.COW, 200);
                        }else{
                            if (RANDOM.nextDouble()<=0.3) resources.put(Hex.ResourceType.SHEEP, 200);
                        }
                    }
                }
            }
        }
    }


    private void generateRivers() {
        int townC = cols / 2;
        int townR = rows / 2;
        int safeRadius = 0;
        int numRivers = 30 + RANDOM.nextInt(20);

        Set<String> usedEdges = new HashSet<>();

        for (int i = 0; i < numRivers; i++) {
            Hex start = findRandomRiverStart(townC, townR, safeRadius);
            if (start == null) continue;

            List<Hex> path = new ArrayList<>();
            path.add(start);

            int length = 2 + RANDOM.nextInt(4);
            Hex current = start;

            for (int s = 0; s < length; s++) {
                List<Hex> neighbours = getNeighbourHexes(current);
                List<Hex> candidates = new ArrayList<>();
                for (Hex n : neighbours) {
                    if (n == null) continue;
                    if (n.getTerrainType() == Hex.TerrainType.SEA
                            || n.getTerrainType() == Hex.TerrainType.TALL_MOUNTAIN) continue;
                    int dc = Math.abs(n.getC() - townC);
                    int dr = Math.abs(n.getR() - townR);
                    if (dc < safeRadius && dr < safeRadius) continue;
                    if (path.contains(n)) continue;
                    String edgeKey = edgeKey(current, n);
                    if (usedEdges.contains(edgeKey)) continue;
                    candidates.add(n);
                }
                if (candidates.isEmpty()) break;
                Hex next = candidates.get(RANDOM.nextInt(candidates.size()));
                usedEdges.add(edgeKey(current, next));
                path.add(next);
                current = next;
            }

            for (int j = 0; j < path.size() - 1; j++) {
                River river = new River(path.get(j), path.get(j + 1), this);
                if (river.isValid()) {
                    path.get(j).getRiversWith().add(river);
                    path.get(j + 1).getRiversWith().add(river);
                }
            }
        }
    }

    private Hex findRandomRiverStart(int townC, int townR, int safeRadius) {
        for (int attempt = 0; attempt < 300; attempt++) {
            int c = RANDOM.nextInt(cols);
            int r = RANDOM.nextInt(rows);
            Hex hex = getHex(c, r);
            if (hex == null) continue;
            if (hex.getTerrainType() == Hex.TerrainType.SEA
                    || hex.getTerrainType() == Hex.TerrainType.TALL_MOUNTAIN) continue;
            int dc = Math.abs(c - townC);
            int dr = Math.abs(r - townR);
            if (dc < safeRadius && dr < safeRadius) continue;
            return hex;
        }
        return null;
    }

    private String edgeKey(Hex a, Hex b) {
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
}
