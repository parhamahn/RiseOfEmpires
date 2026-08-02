package model.mission;

import model.building.edge.Road;
import model.building.tribes.TraderTribe;
import model.building.tribes.Tribe;
import model.map.Hex;
import model.state.GameState;
import model.trade.tribe.TraderTrade;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class TraderMission extends Mission implements Serializable {
private static final long serialVersionUID = 1L;

    public TraderMission(Tribe tribe) {
        super(tribe, 10);
    }

    @Override
    public boolean isAffordable(GameState gameState) {
        Hex start = gameState.getEmpireState().getTownHallHex();
        Hex target = super.getTribe().getHex();
        return isConnectedByRoad(start, target);
    }

    private boolean isConnectedByRoad(Hex start, Hex target) {
        if (start.equals(target)) return true;

        Set<Hex> visited = new HashSet<>();
        visited.add(start);
        Deque<Hex> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Hex current = queue.poll();
            for (Road road : current.getRoadsTo()) {
                Hex neighbour = road.getHexA().equals(current) ? road.getHexB() : road.getHexA();
                if (neighbour.equals(target)) return true;
                if (visited.add(neighbour)) {
                    queue.add(neighbour);
                }
            }
        }
        return false;
    }

    @Override
    public void check(GameState gameState) {
        if (isAffordable(gameState)) {
            super.setStatus(MissionStatus.DONE);
        }
    }

    @Override
    public void processDone(GameState gameState) {
        if (super.getStatus().equals(MissionStatus.DONE)) {
            super.getTribe().changeRelation(super.getTribe().getRelation() + 20, gameState.getEmpireState().getAllies());
            TraderTribe traderTribe = (TraderTribe) super.getTribe();
            TraderTrade tradingStrategy = (TraderTrade) traderTribe.getTradeStrategy();
            tradingStrategy.setMissionBonus(0.1);
        }
    }
}