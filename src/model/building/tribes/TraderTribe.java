package model.building.tribes;

import model.map.Hex;
import model.mission.Mission;
import model.mission.TraderMission;
import model.trade.tribe.TraderTrade;
import model.trade.tribe.TribeTrade;

import java.util.ArrayList;
import java.util.List;

public class TraderTribe extends Tribe {

    private List<Mission> missions;
    private TribeTrade tradeStrategy;

    public TraderTribe(Hex hex) {
        super(hex, "tradertribe");
        missions = new ArrayList<>();
        missions.add(new TraderMission(this));
        tradeStrategy = new TraderTrade();
    }

    public TribeTrade getTradeStrategy() {
        return tradeStrategy;
    }

    public List<Mission> getMissions() {
        return missions;
    }

}
