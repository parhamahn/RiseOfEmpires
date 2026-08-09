package model.mission;

import model.building.Building;
import model.building.edge.Road;
import model.building.tribes.TraderTribe;
import model.building.tribes.Tribe;
import model.map.Hex;
import model.state.GameState;
import model.trade.tribe.TraderTrade;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TraderMission extends Mission implements Serializable {
private static final long serialVersionUID = 1L;

    public TraderMission(Tribe tribe) {
        super(tribe, 10, "Connect a building to the tribe by roads | 10% growth for trading with this tribe");
    }

    @Override
    public boolean isAffordable(GameState gameState) {
        List<Hex> targets = new ArrayList<>();
        targets.add(super.getTribe().getHex());
        for (Hex neighbour : gameState.getWorldState().getMap().getNeighbourHexes(super.getTribe().getHex())) {
            if (neighbour != null) targets.add(neighbour);
        }
        for (Building building : gameState.getEmpireState().getBuildings()) {
            for (Hex target : targets) {
                if (isConnectedByRoad(building.getHex(), target)) return true;
            }
        }
        return false;
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
            super.setStatus(MissionStatus.REWARDED);
        }
    }
}