package model.disasters;

import model.building.Building;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tsunami {
    public static int UNIT_DAMAGE = 30;

    private Hex center;
    private List<Hex> involvedHexes;

    public Tsunami(Map map, Random rand) {
        List<Hex> eligibleCenters = new ArrayList<>();
        for(Hex hex:map.getAllHexes()){
            if(hex.getTerrainType()!= Hex.TerrainType.SEA) {
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
            if(neigh == null) continue;
            for(Hex neigh2:map.getNeighbourHexes(neigh)){
                if(neigh2 != null && neigh2.getTerrainType()==Hex.TerrainType.SEA) {
                    if(!involvedHexes.contains(neigh)){
                        involvedHexes.add(neigh);
                        break;
                    }
                }
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
                    if(unit.getHP()<=0){
                        dead.add(unit);
                    }
                }
            }

            if(hex.getBuilding()!=null && !hex.getBuilding().isTownHall()){
                destroyed.add(hex.getBuilding());
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