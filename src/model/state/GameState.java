package model.state;

import model.map.Map;
import model.map.generator.*;
import model.product.Production;
import model.storage.Storage;

import java.io.Serializable;
import java.util.Random;

public class GameState implements Serializable {
private static final long serialVersionUID = 1L;

    private final EmpireState empireState;
    private final WorldState worldState;
    private Random rng;

    public GameState(Storage storage, Production production) {
        this.rng = new Random();
        this.worldState = new WorldState(new Map(32, 50, rng));
        this.empireState = new EmpireState(storage, production);
        this.empireState.getProduction().setRng(rng);
        restartGame();
    }

    public void restartGame() {
        Map map = worldState.getMap();
        empireState.init(map);
        Generator.generate(this);
    }

    public EmpireState getEmpireState() { return empireState; }
    public WorldState getWorldState() { return worldState; }

    public Random getRng() { return rng; }

    public void reset(){
        Map map = new Map(32, 50, rng);
        empireState.getStorage().reset();
        empireState.getProduction().reset();
        empireState.getBuildings().clear();
        worldState.reset(map);
        empireState.init(map);
        Generator.generate(this);
    }

    public void loadFrom(GameState loaded) {
        this.rng = loaded.getRng() != null ? loaded.getRng() : new Random();
        this.worldState.loadFrom(loaded.getWorldState());
        this.empireState.loadFrom(loaded.getEmpireState());
        this.empireState.getProduction().setRng(this.rng);
        for (model.building.tribes.Tribe tribe : worldState.getTribes()) {
            if (!tribe.isDiscovered()) tribe.setDiscovered(true);
        }
    }

}
