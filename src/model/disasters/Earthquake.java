package model.disasters;

import model.building.Building;
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
        List<Building> destroyed = new ArrayList<Building>();

        for(Hex involved:involvedHexes){
            if(!involved.getUnits().isEmpty()){
                for(Unit unit:involved.getUnits()){
                    unit.setHP(unit.getHP() - UNIT_DAMAGE);
                    if(unit.getHP() <= 0){
                        dead.add(unit);
                    }
                }
            }
            if(involved.getBuilding()!=null){
                involved.getBuilding().setHP(involved.getBuilding().getHP() - BUILDING_DAMAGE);
                if(involved.getBuilding().isTownHall()){
                    if(involved.getBuilding().getHP() <= 0) {
                        involved.getBuilding().setHP(1);
                    }
                }else{
                    if(involved.getBuilding().getHP() <= 0) {
                        destroyed.add(involved.getBuilding());
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


    public List<Hex> getInvolvedHexes() {
        return involvedHexes;
    }

    public Hex getCenter() {
        return center;
    }
}