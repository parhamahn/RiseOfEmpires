package model.disasters;

import model.building.Building;
import model.building.empire.Farm;
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
                }
            }
        }
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
        List<Building> destroyed = new ArrayList<Building>();


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

            if(hex.getBuilding()!=null && !hex.getBuilding().isTownHall()){
                if(hex.getBuilding() instanceof Farm){
                    destroyed.add(hex.getBuilding());
                }else{
                    hex.getBuilding().setHP(hex.getBuilding().getHP()-BUILDING_DAMAGE);
                    hex.getBuilding().setProducingStopped(true);
                    if(hex.getBuilding().getHP()<=0){
                        if(hex.getBuilding().isTownHall()){
                            hex.getBuilding().setHP(1);
                        }else{
                            destroyed.add(hex.getBuilding());
                        }
                    }
                }
            }
        }


        for(Unit unit : dead){
            gameState.getEmpireState().removeUnit(unit);
        }

        for(Building building : destroyed){
            gameState.getEmpireState().removeBuilding(building);
        }
    }

    public Hex getCenter() {
        return center;
    }

    public List<Hex> getInvolvedHexes() {
        return involvedHexes;
    }
}