package controller.handler;

import controller.handler.building.townhall.CookUnitHandler;
import controller.handler.combat.EnemyAttackHandler;
import helpers.sound.SoundEffects;
import model.Season;
import model.building.Building;
import model.building.TradingBuilding;
import model.building.empire.Bazaar;
import model.building.other.TradingCenter;
import model.building.tribes.RelationStatus;
import model.building.tribes.Tribe;
import model.building.tribes.WarriorTribe;
import model.map.Hex;
import model.map.Map;
import model.map.MapPath;
import model.mission.Mission;
import model.mission.MissionStatus;
import model.queue.Queue;
import model.queue.Technology;
import model.state.GameState;
import model.unit.Unit;
import model.unit.combat.Swordsman;
import model.unit.enemy.Guard;
import permanency.SlotID;
import permanency.save.SaveManager;
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
    private final EnemyAttackHandler enemyAttackHandler;

    public TurnHandler(GameState gameState, GamePanel gamePanel, CookUnitHandler cookUnitHandler, BuildingHandler buildingHandler, DisasterHandler disasterHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.disasterHandler = disasterHandler;
        this.enemyAttackHandler = new EnemyAttackHandler(gameState, gamePanel);
    }

    public void endTurnAction() {
        if (hasUnspentActionPoints()) {
            int x = WarningDialogs.unitsWithActionPoints();
            if (x == JOptionPane.NO_OPTION) {
                return;
            }
        }

        gameState.getEmpireState().applyTurnEndHappiness();

        endTurn();
        processQueues();

        enemyAttackHandler.handle(() -> {
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

            try {
                SaveManager.writeSave(gameState, SlotID.AUTO_SAVE, "Auto_Save");
            } catch (Exception e) {
                WarningDialogs.autosaveFailed(e.getMessage());
            }
        });

    }

    private boolean hasUnspentActionPoints() {
        for (Unit unit : gameState.getEmpireState().getUnits()) {
            if (unit.getActionPoints() != 0) {
                return true;
            }
        }
        return false;
    }

    private void processQueues() {
        List<Queue> toRemove = new ArrayList<>();
        int currentTurn = gameState.getWorldState().getTurnNumber();
        boolean mapNeedsRefresh = false;

        for (Queue queue : gameState.getEmpireState().getQueues()) {
            if (queue.isComplete(currentTurn)) {
                if (queue.execute(gameState)) {
                    mapNeedsRefresh = true;
                }
                toRemove.add(queue);
                gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
            }
        }
        gameState.getEmpireState().getQueues().removeAll(toRemove);

        if (mapNeedsRefresh) {
            gamePanel.getMapPanel().markStaticDirty();
            gamePanel.getMapPanel().repaint();
        }
    }

    private void endTurn() {
        disasterHandler.handle();
        resetProductionForNewTurn();
        resetUnitsForNewTurn();
        applyStoredProduction();
        processStarvation();
        boolean anyDamagesToBuilding = processUpkeep();
        processTribes();
        spawnWarriorAllyHelper();
        resetTradingBuildings();
        advanceWorld();
        gameState.getEmpireState().setUpkeepDisaster(anyDamagesToBuilding);
        gameState.getEmpireState().updateTownCapacity();
    }

    private void resetProductionForNewTurn() {
        for (Building building : gameState.getEmpireState().getBuildings()) {
            building.setProducingStopped(false);
        }
        gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
    }

    private void resetUnitsForNewTurn() {
        for (Unit unit : gameState.getEmpireState().getUnits()) {
            unit.resetActionPointsForNewTurn();
            if (gameState.getEmpireState().getHappiness() <= -5) {
                unit.setActionPoints(unit.getActionPoints() - 1);
            }
        }
    }

    private void applyStoredProduction() {
        gameState.getEmpireState().getStorage().applyProduction(gameState.getEmpireState().getProduction());
    }

    private void processStarvation() {
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
    }

    private boolean processUpkeep() {
        boolean anyDamagesToBuilding = false;
        for (Building building : gameState.getEmpireState().getBuildings()) {
            if (building.getUpkeep() != null && !building.getUpkeep().enoughResources(gameState.getEmpireState().getStorage())) {
                Random random = gameState.getRng();
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
        return anyDamagesToBuilding;
    }

    private void processTribes() {
        for (Tribe tribe : gameState.getWorldState().getTribes()) {
            tribe.setUsedThisTurn(false);
            if (tribe.getAllianceLockTurns() > 0) {
                tribe.setAllianceLockTurns(tribe.getAllianceLockTurns() - 1);
            }
            if (tribe.tickActiveMission(gameState)) {
                WarningDialogs.missionFailed(tribe.getName());
            }
            if (tribe.refreshMissions(gameState.getWorldState().getTurnNumber())) {
                WarningDialogs.newTradeOrMission(tribe.getName());
            }
            if (tribe.getRelationStatus() == RelationStatus.ENEMY) {
                long guards = tribe.getHex().getUnits().stream().filter(Guard.class::isInstance).count();
                int guardCap = tribe instanceof WarriorTribe ? 5 : 3;
                if (guards < guardCap && gameState.getWorldState().getTurnNumber() % 3 == 0) {
                    Guard guard = new Guard(tribe);
                    guard.setHex(tribe.getHex());
                    tribe.getHex().getUnits().add(guard);
                    gameState.getWorldState().getEnemies().add(guard);
                    WarningDialogs.tribeProducedUnit(tribe.getName());
                }
                moveGuardsTowardLastAttacker(tribe);
            } else {
                List<Unit> tribeGuards = tribe.getHex().getUnits().stream().filter(Guard.class::isInstance).toList();
                for (Unit guard : tribeGuards) {
                    tribe.getHex().getUnits().remove(guard);
                    gameState.getWorldState().getEnemies().remove(guard);
                }
            }
            if (tribe.getRelationStatus() != RelationStatus.ENEMY && isEmpireBuildingNear(tribe.getHex())) {
                tribe.changeRelation(tribe.getRelation() - 5, gameState.getEmpireState().getAllies());
                if (tribe.getRelationStatus() == RelationStatus.ENEMY) {
                    WarningDialogs.tribeWarning(tribe.getName() + " has become your enemy because you built near its camp!");
                }
            }
        }
    }

    private void moveGuardsTowardLastAttacker(Tribe tribe) {
        Hex lastAttackerHex = tribe.getLastAttackerHex();
        if (lastAttackerHex == null) return;
        int turnsSinceAttack = gameState.getWorldState().getTurnNumber() - tribe.getLastAttackedTurn();
        if (turnsSinceAttack > 2) {
            tribe.markAttacked(null, -1);
            return;
        }
        Map map = gameState.getWorldState().getMap();
        List<Unit> guards = gameState.getWorldState().getEnemies().stream()
                .filter(unit -> unit instanceof Guard && ((Guard) unit).getTribe() == tribe)
                .toList();
        for (Unit guard : guards) {
            if (guard.getActionPoints() <= 0) continue;
            Hex next = MapPath.nextStepToward(map, guard.getHex(), lastAttackerHex, MapPath::isPassableForEnemy);
            if (next == null || next == guard.getHex()) continue;
            guard.getHex().getUnits().remove(guard);
            guard.setHex(next);
            next.getUnits().add(guard);
            guard.setActionPoints(guard.getActionPoints() - 1);
        }
    }

    private void spawnWarriorAllyHelper() {
        boolean warriorAlly = gameState.getEmpireState().getAllies().stream().anyMatch(WarriorTribe.class::isInstance);
        if (warriorAlly && gameState.getWorldState().getTurnNumber() % 3 == 0) {
            Hex townHallHex = gameState.getEmpireState().getTownHallHex();
            Swordsman helper = new Swordsman();
            helper.setOwnerID(gameState.resolveActivePlayerId());
            helper.setHex(townHallHex);
            townHallHex.getUnits().add(helper);
            gameState.getEmpireState().getUnits().add(helper);
        }
    }

    private void resetTradingBuildings() {
        for (TradingCenter tradingCenter : gameState.getWorldState().getTradingCenters()) {
            tradingCenter.setUsedThisTurn(false);
        }
        for (Building building : gameState.getEmpireState().getBuildings()) {
            if (building instanceof Bazaar) {
                Bazaar bazaar = (Bazaar) building;
                bazaar.setUsedThisTurn(false);
            }
        }
    }

    private void advanceWorld() {
        gameState.getWorldState().nextTurn();
        Season.SeasonType old = gameState.getEmpireState().getSeason();
        Season.processSeason(gameState);
        Season.SeasonType newSeason = gameState.getEmpireState().getSeason();
        SoundEffects.getInstance().seasonSoundEffect(old, newSeason);
    }

    private boolean isEmpireBuildingNear(Hex tribeHex) {
        for (model.state.EmpireState empireState : gameState.getEmpireStates().values()) {
            for (Building building : empireState.getBuildings()) {
                if (gameState.getWorldState().getMap().getDistance(building.getHex(), tribeHex) <= 2) {
                    return true;
                }
            }
        }
        return false;
    }



}
