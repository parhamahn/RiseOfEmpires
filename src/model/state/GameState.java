package model.state;

import model.map.Map;
import model.map.generator.*;
import model.product.Production;
import model.storage.Storage;

public class GameState {
    private final EmpireState empireState;
    private final WorldState worldState;

    public GameState(Storage storage, Production production, Map map) {
        this.worldState = new WorldState(map);
        this.empireState = new EmpireState(storage, production);
        restartGame();
    }

    public void restartGame() {
        Map map = worldState.getMap();
        empireState.init(map);
        Generator.generate(this);
    }

    public EmpireState getEmpireState() { return empireState; }
    public WorldState getWorldState() { return worldState; }

    public void reset(){
        Map map = new Map(32, 50);
        empireState.getStorage().reset();
        empireState.getProduction().reset();
        empireState.getBuildings().clear();
        worldState.reset(map);
        empireState.init(map);
        Generator.generate(this);
    }

}
