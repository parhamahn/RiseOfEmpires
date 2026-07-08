package controller.handler;

import model.queue.*;
import model.state.GameState;
import model.building.Building;
import model.unit.Unit;
import view.game.GamePanel;
import view.common.WarningDialogs;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TurnHandler {
    private static final double UPKEEP_DAMAGE_CHANCE = 0.3;
    private static final int MAX_UPKEEP_TURNS = 3;

    private final GameState gameState;
    private final GamePanel gamePanel;
    private final BuildingHandler buildingHandler;

    public TurnHandler(GameState gameState, GamePanel gamePanel, CookUnitHandler cookUnitHandler, BuildingHandler buildingHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.buildingHandler = buildingHandler;
    }

    public void endTurnAction() {
        for (Unit unit : gameState.getEmpireState().getUnits()) {
            if (unit.getActionPoints() != 0) {
                int x = WarningDialogs.unitsWithActionPoints();
                if (x == JOptionPane.NO_OPTION) {
                    return;
                } else {
                    break;
                }
            }
        }
        endTurn();
        processQueues();
        gamePanel.getTownHallPanel().refresh();
        gamePanel.getUnitListPanel().clear();
        gamePanel.getTaskListPanel().clear();
        if (gameState.getEmpireState().isUpkeepDisaster()) {
            WarningDialogs.upkeepDisaster();
        }
        if (gameState.getEmpireState().isStarving()) {
            WarningDialogs.starvationDisaster();
        }
    }

    private void processQueues() {
        List<Queue> toRemove = new ArrayList<>();
        int currentTurn = gameState.getWorldState().getTurnNumber();

        for (Queue queue : gameState.getEmpireState().getQueues()) {
            if (queue.isComplete(currentTurn)) {
                switch (queue.getQueueType()) {
                    case COOK -> {
                        CookQueue cq = (CookQueue) queue;
                        cq.getUnit().setHex(gameState.getEmpireState().getTownHallHex());
                        gameState.getEmpireState().getTownHallHex().getUnits().add(cq.getUnit());
                        gameState.getEmpireState().getUnits().add(cq.getUnit());
                        toRemove.add(queue);
                    }
                    case UPGRADE -> {
                        UpgradeQueue uq = (UpgradeQueue) queue;
                        gameState.getEmpireState().setTownHallLevel(uq.getTargetLevel());
                        toRemove.add(queue);
                    }
                    case RESEARCH -> {
                        ResearchQueue rq = (ResearchQueue) queue;
                        gameState.getEmpireState().getResearchedTechnologies().add(rq.getTechnology());
                        toRemove.add(queue);
                    }
                }
                gameState.getEmpireState().recalculateProduction();
            }
        }
        gameState.getEmpireState().getQueues().removeAll(toRemove);
    }

    private void endTurn() {
        gameState.getEmpireState().recalculateProduction();

        for (Unit unit : gameState.getEmpireState().getUnits()) {
            unit.resetActionPointsForNewTurn();
        }

        gameState.getEmpireState().getStorage().setFoodQuantity(Math.max(Math.min(gameState.getEmpireState().getStorage().getFoodMax(), gameState.getEmpireState().getProduction().getFoodProduction() + gameState.getEmpireState().getStorage().getFoodQuantity()), 0));
        gameState.getEmpireState().getStorage().setWoodQuantity(Math.max(Math.min(gameState.getEmpireState().getStorage().getWoodMax(), gameState.getEmpireState().getProduction().getWoodProduction() + gameState.getEmpireState().getStorage().getWoodQuantity()), 0));
        gameState.getEmpireState().getStorage().setStoneQuantity(Math.max(Math.min(gameState.getEmpireState().getStorage().getStoneMax(), gameState.getEmpireState().getProduction().getStoneProduction() + gameState.getEmpireState().getStorage().getStoneQuantity()), 0));
        gameState.getEmpireState().getStorage().setIronQuantity(Math.max(Math.min(gameState.getEmpireState().getStorage().getIronMax(), gameState.getEmpireState().getProduction().getIronProduction() + gameState.getEmpireState().getStorage().getIronQuantity()), 0));

        if (gameState.getEmpireState().getStorage().getFoodQuantity() == 0 && gameState.getEmpireState().getProduction().getFoodProduction() < 0) {
            for (Unit unit : gameState.getEmpireState().getUnits()) {
                unit.setActionPoints(Math.max(0, unit.getActionPoints() * 2 / 3));
            }
            for (Queue queue : gameState.getEmpireState().getQueues()) {
                queue.addDelay(1);
            }
            gameState.getEmpireState().setStarving(true);
        } else {
            gameState.getEmpireState().setStarving(false);
        }

        boolean anyDamagesToBuilding = false;
        for (Building building : gameState.getEmpireState().getBuildings()) {
            if (building.getUpkeep() != null && !building.getUpkeep().enoughResources(gameState.getEmpireState().getStorage())) {
                Random random = new Random();
                boolean damageBuilding = random.nextDouble() <= UPKEEP_DAMAGE_CHANCE;
                if (damageBuilding) {
                    anyDamagesToBuilding = true;
                    building.setNoUpkeepTurns(building.getNoUpkeepTurns() + 1);
                    if (building.getNoUpkeepTurns() == MAX_UPKEEP_TURNS) {
                        building.getHex().setBuilding(null);
                        for (Unit unit : building.getAssignedWorkers()) unit.setAssigned(false);
                        building.getAssignedWorkers().clear();
                        gameState.getEmpireState().getBuildings().remove(building);
                    }
                }
            }
        }
        gameState.getEmpireState().setUpkeepDisaster(anyDamagesToBuilding);
        buildingHandler.updateTownCapacity();
        gameState.getWorldState().nextTurn();
    }
}
