package model.building.tribes;

import model.map.Hex;
import model.mission.Mission;
import model.mission.MountainMission;
import model.trade.tribe.MountainTrade;
import model.trade.tribe.TribeTrade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MountainTribe extends Tribe implements Serializable {
private static final long serialVersionUID = 1L;

    private List<Mission> missions;
    private TribeTrade tradeStrategy;

    public MountainTribe(Hex hex) {
        super(hex, 50);
        this.missions = new ArrayList<>();
        this.missions.add(new MountainMission(this));
        this.tradeStrategy = new MountainTrade();
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public TribeTrade getTradeStrategy() {
        return tradeStrategy;
    }
}
