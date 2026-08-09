package model.building.tribes;

import model.map.Hex;
import model.mission.Mission;
import model.mission.WarriorMission;
import model.trade.tribe.TribeTrade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WarriorTribe extends Tribe implements Serializable {
private static final long serialVersionUID = 1L;

    private List<Mission> missions;
    private TribeTrade tradingStrategy;

    public WarriorTribe(Hex hex) {
        super(hex, "warriortribe", 70);
        this.missions = new ArrayList<>();
        this.missions.add(new WarriorMission(this));
        this.tradingStrategy = null;
    }

    public TribeTrade getTradingStrategy() {return this.tradingStrategy;}

    public List<Mission> getMissions() {
        return missions;
    }

}
