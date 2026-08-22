package model.map;

import java.io.*;
import java.util.List;

public class TextMap {

    public static void fromText(String path){
        try (InputStream in = TextMap.class.getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            List<String> lines = reader.lines().toList();
            int rows = lines.size();
            int cols = rows > 0 ? lines.get(0).length() : 0;

            System.out.println(rows + ", " + cols);

        } catch (Exception e) {
            System.out.println(e.toString());
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
