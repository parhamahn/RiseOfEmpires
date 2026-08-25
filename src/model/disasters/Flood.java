package model.disasters;

import model.MapStructure;
import model.building.Building;
import model.building.empire.Farm;
import model.building.tribes.Tribe;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flood {
    public static int UNIT_DAMAGE = 20;
    public static int BUILDING_DAMAGE = 30;

    private Hex center;
    private List<Hex> involvedHexes;

    public Flood(Map map, Random rand) {
        List<Hex> eligibleCenters = new ArrayList<>();
        for(Hex hex:map.getAllHexes()){
            if(hex.getTerrainType()== Hex.TerrainType.FOREST || hex.getTerrainType()== Hex.TerrainType.PLAIN || hex.getTerrainType()== Hex.TerrainType.MEADOW) {
                for (Hex neigh : map.getNeighbourHexes(hex)) {
                    if (neigh != null && neigh.getTerrainType() == Hex.TerrainType.SEA) {
                        eligibleCenters.add(hex);
                        break;
                    }
                    if(hex.hasRiverTo(neigh)){
                        eligibleCenters.add(hex);
                        break;
                    }
                }
            }
        }
        if (eligibleCenters.isEmpty()) {return;}
        this.center = eligibleCenters.get(rand.nextInt(eligibleCenters.size()));
        involve(this.center, map);
    }

    public void involve(Hex center, Map map) {
        List<Hex> involvedHexes = new ArrayList<>();
        involvedHexes.add(center);
        for(Hex neigh:map.getNeighbourHexes(center)){
            if(neigh != null && neigh.getTerrainType()!= Hex.TerrainType.SEA && neigh.getTerrainType()!= Hex.TerrainType.MOUNTAIN &&  neigh.getTerrainType()!= Hex.TerrainType.TALL_MOUNTAIN){
                involvedHexes.add(neigh);
            }
        }
        this.involvedHexes = involvedHexes;
    }

    public void effect(GameState gameState){
        List<Unit> dead = new ArrayList<Unit>();
        List<MapStructure> destroyed = new ArrayList<MapStructure>();


        for(Hex hex:involvedHexes){
            if(!hex.getUnits().isEmpty()){
                for(Unit unit:hex.getUnits()){
                    unit.setHP(unit.getHP()-UNIT_DAMAGE);
                    unit.setActionPoints(0);
                    if(unit.getHP()<=0){
                        dead.add(unit);
                    }
                }
            }

            MapStructure structure = hex.getBuilding();
            if(structure!=null && !structure.isTownHall()){
                if(structure instanceof Farm){
                    destroyed.add(structure);
                }else{
                    structure.setHP(structure.getHP()-BUILDING_DAMAGE);
                    if(structure instanceof Building building){
                        building.setProducingStopped(true);
                    }
                    if(structure.getHP()<=0){
                        destroyed.add(structure);
                    }
                }
            }
        }


        for(Unit unit : dead){
            gameState.removeDeadUnit(unit);
        }

        for(MapStructure structure : destroyed){
            gameState.removeDestroyedStructure(structure);
        }
    }

    public Hex getCenter() {
        return center;
    }

    public List<Hex> getInvolvedHexes() {
        return involvedHexes;
    }
}