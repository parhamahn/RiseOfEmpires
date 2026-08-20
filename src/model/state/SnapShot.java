package model.state;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SnapShot {


    private Map<UUID, EmpireState> empireStates = new HashMap<>();
    private WorldState worldState;

    public SnapShot(WorldState worldState) {
        this.worldState = worldState;
    }

    public Map<UUID, EmpireState> getEmpireStates() {
        return empireStates;
    }

    public WorldState getWorldState() {
        return worldState;
    }
}
