package model.mission;

import model.building.tribes.Tribe;
import model.state.GameState;

import java.io.Serializable;

public abstract class Mission implements Serializable {
private static final long serialVersionUID = 1L;

    private final Tribe tribe;
    private int deadline;
    private final int startingDeadline;
    private MissionStatus status;
    private String summary;

    public Mission(Tribe tribe, int deadline, String summary) {
        this.tribe = tribe;
        this.deadline = deadline;
        this.startingDeadline = deadline;
        this.summary = summary;
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

    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {}

    public void decreaseDeadline() {
        deadline--;
    }

    public void resetDeadline() {
        deadline = startingDeadline;
    }

    public int getDeadline() {
        return deadline;
    }
}
