package model.disasters;

import model.MapStructure;
import model.building.Building;
import model.building.tribes.Tribe;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Earthquake {
    public static int UNIT_DAMAGE = 10;
    public static int BUILDING_DAMAGE = 50;

    private Hex center;
    private List<Hex> involvedHexes;

    public Earthquake(Map map, Random rand) {
        this.center = map.getAllHexes().get(rand.nextInt(map.getAllHexes().size()));
        involve(this.center, map);
    }

    public void involve(Hex center, Map map) {
        List<Hex> involvedHexes = new ArrayList<>();
        involvedHexes.add(center);
        for(Hex neigh:map.getNeighbourHexes(center)){
            if(neigh == null) continue;
            for(Hex neigh2:map.getNeighbourHexes(neigh)){
                if(neigh2 != null && !involvedHexes.contains(neigh2)){involvedHexes.add(neigh2);}
            }
            if(!involvedHexes.contains(neigh)){involvedHexes.add(neigh);}
        }
        this.involvedHexes = involvedHexes;
    }


    public void effect(GameState gameState) {
        List<Unit> dead = new ArrayList<Unit>();
        List<MapStructure> destroyed = new ArrayList<MapStructure>();

        for(Hex involved:involvedHexes){
            if(!involved.getUnits().isEmpty()){
                for(Unit unit:involved.getUnits()){
                    unit.setHP(unit.getHP() - UNIT_DAMAGE);
                    if(unit.getHP() <= 0){
                        dead.add(unit);
                    }
                }
            }
            MapStructure structure = involved.getBuilding();
            if(structure!=null){
                structure.setHP(structure.getHP() - BUILDING_DAMAGE);
                if(structure.isTownHall()){
                    if(structure.getHP() <= 0) {
                        structure.setHP(1);
                    }
                }else{
                    if(structure.getHP() <= 0) {
                        destroyed.add(structure);
                    }
                }
            }
        }
        for(Unit unit : dead){
            gameState.getEmpireState().removeUnit(unit);
        }

        for(MapStructure structure : destroyed){
            if (structure instanceof Building building) {
                gameState.getEmpireState().removeBuilding(building);
            } else if (structure instanceof Tribe tribe) {
                tribe.destroyCamp(gameState);
            }
        }
    }

    public List<Hex> getInvolvedHexes() {
        return involvedHexes;
    }

    public Hex getCenter() {
        return center;
    }
}