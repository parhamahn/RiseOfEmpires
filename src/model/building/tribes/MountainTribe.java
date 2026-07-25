package model.building.tribes;

import model.map.Hex;
import model.mission.Mission;
import model.mission.MountainMission;
import model.trade.tribe.MountainTrade;
import model.trade.tribe.TribeTrade;

import java.util.ArrayList;
import java.util.List;

public class MountainTribe extends Tribe {

    private List<Mission> missions;
    private TribeTrade tradeStrategy;

    public MountainTribe(Hex hex) {
        super(hex, "mountaintribe");
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
