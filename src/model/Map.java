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
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int randomIndex = (int) (Math.random() * terrains.length);
                Hex hex = new Hex(c, r, terrains[randomIndex]);
                hex.setDiscovered(true);
                grid[r][c] = hex;
            }
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
