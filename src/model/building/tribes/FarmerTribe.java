package model.building.tribes;

import model.map.Hex;
import model.mission.FarmerMission;
import model.mission.Mission;
import model.trade.tribe.FarmerTrade;
import model.trade.tribe.TribeTrade;

import java.util.ArrayList;
import java.util.List;

public class FarmerTribe extends Tribe {

    private List<Mission> missions;
    private TribeTrade tradeStrategy;

    public FarmerTribe(Hex hex) {
        super(hex, "farmertribe");
        this.missions = new ArrayList<>();
        this.missions.add(new FarmerMission(this));
        this.tradeStrategy = new FarmerTrade();
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public TribeTrade getTradeStrategy() {
        return tradeStrategy;
    }
}
