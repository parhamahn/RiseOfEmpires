package model.map;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextMap {

    public static Map fromText(String path){
        try (InputStream in = TextMap.class.getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            List<String> lines = reader.lines().toList();
            int rows = lines.size();
            int cols = rows > 0 ? lines.get(0).length() : 0;

            Map map = new Map(rows, cols);
            List<Hex> spawns = new ArrayList<>();

            for (int r = 0; r < rows; r++) {
                String line = lines.get(r);
                if (line.length() != cols)
                    throw new IllegalStateException(
                            "Map " + path + ": row " + r + " has length " + line.length() + ", expected " + cols);

                for (int c = 0; c < cols; c++) {
                    char ch = line.charAt(c);
                    Hex hex;
                    if (Character.isDigit(ch)) {
                        int spawnNumber = ch - '0';
                        if (spawnNumber < 1 || spawnNumber > 3)
                            throw new IllegalStateException("Map " + path + ": bad spawn digit '" + ch + "' at " + c + "," + r);
                        hex = new Hex(c, r, Hex.TerrainType.PLAIN);
                        spawns.add(hex);
                    } else {
                        hex = new Hex(c, r, getTerrain(ch));
                    }
                    map.setHex(r, c, hex);
                }
            }
            map.setSpawnPoints(spawns);
            return map;

        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public static char getTerrainChar(Hex.TerrainType terrainType){
        char x = 0;
        switch (terrainType){
            case PLAIN -> {x= 'P';}
            case SEA -> {x= 'S';}
            case TALL_MOUNTAIN -> {x= 'T';}
            case FOREST -> {x= 'F';}
            case MOUNTAIN -> {x= 'M';}
            case MEADOW -> {x= 'm';}
        }
        return x;
    }

    public static Hex.TerrainType getTerrain(char x){
        return switch (x) {
            case 'P' -> Hex.TerrainType.PLAIN;
            case 'S' -> Hex.TerrainType.SEA;
            case 'T' -> Hex.TerrainType.TALL_MOUNTAIN;
            case 'F' -> Hex.TerrainType.FOREST;
            case 'M' -> Hex.TerrainType.MOUNTAIN;
            case 'm' -> Hex.TerrainType.MEADOW;
            default -> throw new IllegalStateException("Unexpected value: " + x);
        };
    }

    public static void toText(String path, Map map){
        try(FileWriter fileWriter = new FileWriter(path)){

            for(int i=0;i<map.getRows();i++){
                for(int j=0;j< map.getCols();j++){
                    fileWriter.append(getTerrainChar(map.getHex(j, i).getTerrainType()));
                }
                fileWriter.append("\n");
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

}
