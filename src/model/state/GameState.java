package model.state;

import com.google.gson.Gson;
import model.map.Hex;
import model.map.Map;
import model.map.TextMap;
import model.map.generator.*;
import model.building.tribes.Tribe;
import model.unit.enemy.Guard;
import model.product.Production;
import model.storage.Storage;
import network.dto.GameStateDTO;
import network.dto.mapper.GameStateMapper;

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

    public void recordTribeAttack(Hex attackerHex, Hex defenderHex) {
        for (Tribe tribe : worldState.getTribes()) {
            boolean campHit = tribe.getHex() == defenderHex;
            boolean guardHit = defenderHex.getUnits().stream()
                    .anyMatch(unit -> unit instanceof Guard && ((Guard) unit).getTribe() == tribe);
            if (campHit || guardHit) {
                tribe.markAttacked(attackerHex, worldState.getTurnNumber());
            }
        }
    }

    public Random getRng() { return rng; }

    public void reset(){
        Map map = new Map(32, 50, rng);
        empireState.getStorage().reset();
        empireState.getProduction().reset();
        empireState.reset();
        worldState.reset(map);
        empireState.init(map);
        Generator.generate(this);
    }

    public void loadFrom(GameState loaded) {
        this.rng = loaded.getRng() != null ? loaded.getRng() : new Random();
        this.worldState.loadFrom(loaded.getWorldState());
        this.empireState.loadFrom(loaded.getEmpireState());
        this.empireState.getProduction().setRng(this.rng);
    }

}
