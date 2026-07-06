package model.state;

import model.Map;
import model.Production;
import model.Storage;

public class GameState {
    private final EmpireState empireState;
    private final WorldState worldState;

    public GameState(Storage storage, Production production, Map map) {
        this.worldState = new WorldState(map);
        this.empireState = new EmpireState(storage, production);
        restartGame();
    }

    public void restartGame() {
        empireState.init(worldState.getMap());
    }

    public EmpireState getEmpireState() { return empireState; }
    public WorldState getWorldState() { return worldState; }
}
