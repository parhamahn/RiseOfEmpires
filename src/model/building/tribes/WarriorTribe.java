package model.building.tribes;

import model.map.Hex;
import model.mission.Mission;
import model.mission.WarriorMission;
import model.storage.Storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WarriorTribe extends Tribe implements Serializable {
private static final long serialVersionUID = 1L;

    private List<Mission> missions;

    public WarriorTribe(Hex hex) {
        super(hex, 70);
        this.missions = new ArrayList<>();
        this.missions.add(new WarriorMission(this));
    }

    public List<Mission> getMissions() {
        return missions;
    }

    @Override
    public boolean isAllianceBlocked(List<Tribe> allies) {
        return !allies.isEmpty();
    }

    @Override
    protected void collectLoot(Storage storage) {
        storage.setIronQuantity(storage.getIronQuantity() + 30);
    }
}
