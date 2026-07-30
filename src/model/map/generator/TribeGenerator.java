package model.map.generator;

import model.building.Building;
import model.building.tribes.*;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class TribeGenerator {

    public static int farmerTribeCount = 3;
    public static int traderTribeCount = 3;
    public static int warriorTribeCount = 3;
    public static int mountainTribeCount = 3;
    public static int beachTribeCount = 3;

    public static Random rand = new Random();

    public static void generateTribes(GameState gameState){

        Map map = gameState.getWorldState().getMap();

        List<Hex> plainHexes = new ArrayList<>();
        List<Hex> meadowHexes = new ArrayList<>();
        List<Hex> mountainHexes = new ArrayList<>();
        List<Hex> beachHexes = new ArrayList<>();
        for(Hex hex:map.getAllHexes()){
            switch (hex.getTerrainType()){
                case PLAIN -> plainHexes.add(hex);
                case MOUNTAIN -> mountainHexes.add(hex);
                case MEADOW -> meadowHexes.add(hex);
            }
            for(Hex neighbour:map.getNeighbourHexes(hex)){
                if(neighbour!=null&&neighbour.getTerrainType()== Hex.TerrainType.SEA){
                    beachHexes.add(neighbour);
                }
            }
        }
        List<Hex> plainAndMeadowHexes = Stream.concat(plainHexes.stream(), meadowHexes.stream()).toList();

        placeTribes(farmerTribeCount, plainAndMeadowHexes, hex -> new FarmerTribe(hex), rand, gameState);
        placeTribes(mountainTribeCount, mountainHexes, hex -> new MountainTribe(hex), rand, gameState);
        placeTribes(beachTribeCount, beachHexes, hex -> new BeachTribe(hex), rand, gameState);
        placeTribes(warriorTribeCount, map.getAllHexes(), hex -> new WarriorTribe(hex), rand, gameState);
        placeTribes(traderTribeCount, map.getAllHexes(), hex -> new TraderTribe(hex), rand, gameState);

    }

    private static void placeTribes(int count, List<Hex> candidates, java.util.function.Function<Hex, Building> factory, Random rand, GameState gameState) {
        int placed = 0;
        int maxAttempts = count * 100;
        int attempts = 0;
        while (placed < count && attempts < maxAttempts) {
            Hex hex = candidates.get(rand.nextInt(candidates.size()));
            attempts++;
            if (hex.getBuilding() == null) {
                Building building = factory.apply(hex);
                hex.setBuilding(building);
                gameState.getWorldState().getTribes().add((Tribe) building);
                placed++;
            }
        }
    }
}
