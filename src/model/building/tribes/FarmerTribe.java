package model.building.tribes;

import model.map.Hex;
import model.mission.FarmerMission;
import model.mission.Mission;
import model.trade.tribe.FarmerTrade;
import model.trade.tribe.TribeTrade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FarmerTribe extends Tribe implements Serializable {
    private static final long serialVersionUID = 1L;
    public static double FOOD_PRODUCTION_BOOST = 1.2;

    private List<Mission> missions;
    private TribeTrade tradeStrategy;

    public FarmerTribe(Hex hex) {
        super(hex, 40);
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
