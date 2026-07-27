package model.building.tribes;

import model.map.Hex;
import model.mission.Mission;
import model.mission.WarriorMission;

import java.util.ArrayList;
import java.util.List;

public class WarriorTribe extends Tribe {

    private List<Mission> missions;

    public WarriorTribe(Hex hex) {
        super(hex, "warriortribe", 70);
        this.missions = new ArrayList<>();
        this.missions.add(new WarriorMission(this));
    }

    public List<Mission> getMissions() {
        return missions;
    }
}
