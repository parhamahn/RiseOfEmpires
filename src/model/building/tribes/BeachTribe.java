package model.building.tribes;

import model.map.Hex;
import model.mission.BeachMission;
import model.mission.Mission;
import model.trade.tribe.BeachTrade;
import model.trade.tribe.TribeTrade;

import java.util.ArrayList;
import java.util.List;

public class BeachTribe extends Tribe {

    private List<Mission> missions;
    private TribeTrade tradeStrategy;

    public BeachTribe(Hex hex) {
        super(hex, "beachtribe");
        this.missions = new ArrayList<>();
        this.missions.add(new BeachMission(this));
        this.tradeStrategy = new BeachTrade();
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public TribeTrade getTradeStrategy() {
        return tradeStrategy;
    }
}
