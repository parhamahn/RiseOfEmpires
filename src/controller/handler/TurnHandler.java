package controller.handler;

import controller.handler.building.townhall.CookUnitHandler;
import model.Season;
import model.TownHallLevel;
import model.building.Building;
import model.building.TradingBuilding;
import model.queue.*;
import model.state.GameState;
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
    private final DisasterHandler disasterHandler;

    public TurnHandler(GameState gameState, GamePanel gamePanel, CookUnitHandler cookUnitHandler, BuildingHandler buildingHandler, DisasterHandler disasterHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.disasterHandler = disasterHandler;
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

        for(Building building:gameState.getEmpireState().getBuildings()){
            if(building instanceof TradingBuilding){
                ((TradingBuilding) building).setUsedThisTurn(false);
            }
        }

        endTurn();
        processQueues();
        gamePanel.getTownHallPanel().refresh();
        gamePanel.getHudPanel().refresh();
        gamePanel.getUnitListPanel().clear();
        gamePanel.getTaskListPanel().clear();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.repaint();
        if (gameState.getEmpireState().isUpkeepDisaster()) {
            WarningDialogs.upkeepDisaster();
        }
        if (gameState.getEmpireState().isStarving()) {
            WarningDialogs.starvationDisaster();
        }

        gameState.getEmpireState().recalculateHappiness();
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
                        TownHallLevel newLevel = uq.getTargetLevel();
                        gameState.getEmpireState().setTownHallLevel(newLevel);
                        gameState.getEmpireState().getStorage().upgradeStorage(newLevel.getMaxStorage());
                        gameState.getEmpireState().getTownHallHex().getBuilding().setHP(newLevel.getMaxHP());
                        gameState.getEmpireState().getTownHallHex().getBuilding().setMaxHP(newLevel.getMaxHP());
                        gamePanel.getHudPanel().refresh();
                        toRemove.add(queue);
                    }
                    case RESEARCH -> {
                        ResearchQueue rq = (ResearchQueue) queue;
                        gameState.getEmpireState().getResearchedTechnologies().add(rq.getTechnology());
                        if (rq.getTechnology() == Technology.DEFENSIVE_PATTERN) {
                            ProcessTechnology.processDefensivePattern(gameState);
                            gamePanel.getMapPanel().markStaticDirty();
                            gamePanel.getMapPanel().repaint();
                        }
                        toRemove.add(queue);
                    }
                }
                gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
            }
        }
        gameState.getEmpireState().getQueues().removeAll(toRemove);
    }

    private void endTurn() {

        disasterHandler.handle();

        for(Building building:gameState.getEmpireState().getBuildings()){building.setProducingStopped(false);}
        gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());

        for (Unit unit : gameState.getEmpireState().getUnits()) {
            unit.resetActionPointsForNewTurn();
            if(gameState.getEmpireState().getHappiness()<=-5){unit.setActionPoints(unit.getActionPoints() - 1);}
        }

        gameState.getEmpireState().getStorage().applyProduction(gameState.getEmpireState().getProduction());

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
                        gameState.getEmpireState().removeBuilding(building);
                    }
                }
            }
        }
        gameState.getWorldState().nextTurn();
        Season.processSeason(gameState);
        gameState.getEmpireState().setUpkeepDisaster(anyDamagesToBuilding);
        gameState.getEmpireState().updateTownCapacity();
    }



}
