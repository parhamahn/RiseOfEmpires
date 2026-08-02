package model.building.tribes;

import model.map.Hex;
import model.mission.Mission;
import model.mission.TraderMission;
import model.trade.tribe.TraderTrade;
import model.trade.tribe.TribeTrade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TraderTribe extends Tribe implements Serializable {
private static final long serialVersionUID = 1L;

    private List<Mission> missions;
    private TribeTrade tradeStrategy;

    public TraderTribe(Hex hex) {
        super(hex, "tradertribe", 50);
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
