package model.mission;

import model.building.empire.Dock;
import model.building.tribes.Tribe;
import model.map.Hex;
import model.state.GameState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BeachMission extends Mission implements Serializable {
private static final long serialVersionUID = 1L;

    public BeachMission(Tribe tribe) {
        super(tribe, 10, "Instruct a dock in tribe's area | You get 30 food and 50% off your next dock");
    }

    @Override
    public boolean isAffordable(GameState gameState) {
        boolean affordable = false;
        List<Hex> toCheck = new ArrayList<>();
        toCheck.add(super.getTribe().getHex());
        for(Hex neighbor1: gameState.getWorldState().getMap().getNeighbourHexes(super.getTribe().getHex())){
            toCheck.add(neighbor1);
            for(Hex neighbor2:gameState.getWorldState().getMap().getNeighbourHexes(neighbor1)) {
                if(!toCheck.contains(neighbor2)) {toCheck.add(neighbor2);}
                for(Hex neighbor3:gameState.getWorldState().getMap().getNeighbourHexes(neighbor2)) {
                    if(!toCheck.contains(neighbor3)) {toCheck.add(neighbor3);}
                    for(Hex neighbor4:gameState.getWorldState().getMap().getNeighbourHexes(neighbor3)) {
                        if(!toCheck.contains(neighbor4)) {toCheck.add(neighbor4);}
                    }
                }
            }
        }
        for(Hex hex: toCheck) {
            if (hex.getBuilding() != null && hex.getBuilding() instanceof Dock) {
                affordable = true;
                break;
            }
        }
        return affordable;
    }

    @Override
    public void check(GameState gameState) {
        if(isAffordable(gameState)) {
            super.setStatus(MissionStatus.DONE);
        }
    }

    @Override
    public void processDone(GameState gameState) {
        if(super.getStatus() == MissionStatus.DONE) {
            gameState.getEmpireState().getStorage().setFoodQuantity(gameState.getEmpireState().getStorage().getFoodQuantity()+30);
            gameState.getEmpireState().setDockDiscount(true);
            super.setStatus(MissionStatus.REWARDED);
        }
    }
}
