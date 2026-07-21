package model.mission;

import model.building.tribes.Tribe;
import model.state.GameState;

public abstract class Mission {

    private final Tribe tribe;
    private int deadline;
    private MissionStatus status;

    public Mission(Tribe tribe, int deadline) {
        this.tribe = tribe;
        this.deadline = deadline;
        this.status = MissionStatus.AVAILABLE;
    }

    public abstract boolean isAffordable(GameState gameState);
    public abstract void check(GameState gameState);
    public abstract void processDone(GameState gameState);

    public Tribe getTribe() {return tribe;}

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public void decreaseDeadline() {
        deadline--;
    }

    public int getDeadline() {
        return deadline;
    }
}
