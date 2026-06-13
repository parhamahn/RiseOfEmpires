package model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Storage storage;
    private Production production;
    private Hex selectedHex;
    private List<Building> buildings;
    private Map map;


    public GameState(Storage storage, Production production, Map map){
        this.storage = storage;
        this.production = production;
        this.selectedHex = null;
        this.map = map;
        buildings = new ArrayList<>();
        buildings.add(new TownHall(map.getHex(map.getCols()/2, map.getRows()/2)));
    }


    public Hex getSelectedHex() {
        return selectedHex;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setSelectedHex(Hex selectedHex) {
        this.selectedHex = selectedHex;
    }
}
