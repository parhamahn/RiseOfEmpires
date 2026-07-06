package controller.handler;

import model.CookQueue;
import model.GameState;
import model.building.Building;
import model.unit.Unit;
import view.GamePanel;
import view.WarningDialogs;

import javax.swing.*;
import java.util.Random;

public class TurnHandler {
    private static final double UPKEEP_DAMAGE_CHANCE = 0.3;
    private static final int MAX_UPKEEP_TURNS = 3;

    private final GameState gameState;
    private final GamePanel gamePanel;
    private final CookUnitHandler cookUnitHandler;

    public TurnHandler(GameState gameState, GamePanel gamePanel, CookUnitHandler cookUnitHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.cookUnitHandler = cookUnitHandler;
    }

    public void endTurnAction() {
        for (Unit unit : gameState.getUnits()) {
            if (unit.getActionPoints() != 0) {
                int x = WarningDialogs.unitsWithActionPoints(gamePanel);
                if (x == JOptionPane.NO_OPTION) {
                    return;
                } else {
                    break;
                }
            }
        }
        endTurn();
        cookUnitHandler.checkCooks(gameState);
        gamePanel.getTownHallPanel().refresh();
        gamePanel.getUnitListPanel().clear();
        gamePanel.getTaskListPanel().clear();
        if (gameState.isUpkeepDisaster()) {
            WarningDialogs.upkeepDisaster(gamePanel);
        }
        if (gameState.isStarving()) {
            WarningDialogs.starvationDisaster(gamePanel);
        }
    }

    private void endTurn() {
        gameState.getProduction().recalculate(gameState);

        for (Unit unit : gameState.getUnits()) {
            unit.resetActionPointsForNewTurn();
        }

        gameState.getStorage().setFoodQuantity(Math.max(Math.min(gameState.getStorage().getFoodMax(), gameState.getProduction().getFoodProduction() + gameState.getStorage().getFoodQuantity()), 0));
        gameState.getStorage().setWoodQuantity(Math.max(Math.min(gameState.getStorage().getWoodMax(), gameState.getProduction().getWoodProduction() + gameState.getStorage().getWoodQuantity()), 0));
        gameState.getStorage().setStoneQuantity(Math.max(Math.min(gameState.getStorage().getStoneMax(), gameState.getProduction().getStoneProduction() + gameState.getStorage().getStoneQuantity()), 0));
        gameState.getStorage().setIronQuantity(Math.max(Math.min(gameState.getStorage().getIronMax(), gameState.getProduction().getIronProduction() + gameState.getStorage().getIronQuantity()), 0));

        if (gameState.getStorage().getFoodQuantity() == 0 && gameState.getProduction().getFoodProduction() < 0) {
            for (Unit unit : gameState.getUnits()) {
                unit.setActionPoints(Math.max(0, unit.getActionPoints() * 2 / 3));
            }
            for (CookQueue cookQueue : gameState.getUnitQueues()) {
                cookQueue.setTurnToCook(cookQueue.getTurnToCook() + 1);
            }
            gameState.setStarving(true);
        } else {
            gameState.setStarving(false);
        }

        boolean anyDamagesToBuilding = false;
        for (Building building : gameState.getBuildings()) {
            if (building.getUpkeep() != null && !building.getUpkeep().enoughResources(gameState.getStorage())) {
                Random random = new Random();
                boolean damageBuilding = random.nextDouble() <= UPKEEP_DAMAGE_CHANCE;
                if (damageBuilding) {
                    anyDamagesToBuilding = true;
                    building.setNoUpkeepTurns(building.getNoUpkeepTurns() + 1);
                    if (building.getNoUpkeepTurns() == MAX_UPKEEP_TURNS) {
                        building.getHex().setBuilding(null);
                        for (Unit unit : building.getAssignedWorkers()) unit.setAssigned(false);
                        building.getAssignedWorkers().clear();
                        gameState.getBuildings().remove(building);
                    }
                }
            }
        }
        gameState.setUpkeepDisaster(anyDamagesToBuilding);
        gameState.updateTownCapacity();
        gameState.nextTurn();
    }
}
