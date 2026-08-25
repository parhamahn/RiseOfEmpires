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
import model.state.EmpireState;
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
import java.util.UUID;

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
        EmpireState actor = gameState.getEmpireState();
        if (hasUnspentActionPoints(actor)) {
            int x = WarningDialogs.unitsWithActionPoints();
            if (x == JOptionPane.NO_OPTION) {
                return;
            }
        }

        actor.applyTurnEndHappiness();

        endTurnFor(actor);
        if (gameState.isLastInRound()) {
            worldTick();
        }
        processQueues(actor);

        gameState.nextPlayerTurn();

        enemyAttackHandler.handle(() -> {
            gamePanel.getTownHallPanel().refresh();
            gamePanel.getHudPanel().refresh();
            gamePanel.getUnitListPanel().clear();
            gamePanel.getTaskListPanel().clear();
            gamePanel.getMapPanel().markStaticDirty();
            gamePanel.repaint();
            if (actor.isUpkeepDisaster()) {
                WarningDialogs.upkeepDisaster();
            }
            if (actor.isStarving()) {
                WarningDialogs.starvationDisaster();
            }

            try {
                SaveManager.writeSave(gameState, SlotID.AUTO_SAVE, "Auto_Save");
            } catch (Exception e) {
                WarningDialogs.autosaveFailed(e.getMessage());
            }
        });

    }

    private boolean hasUnspentActionPoints(EmpireState empire) {
        for (Unit unit : empire.getUnits()) {
            if (unit.getActionPoints() != 0) {
                return true;
            }
        }
        return false;
    }

    private void processQueues(EmpireState empire) {
        List<Queue> toRemove = new ArrayList<>();
        int currentTurn = gameState.getWorldState().getTurnNumber();
        boolean mapNeedsRefresh = false;

        for (Queue queue : empire.getQueues()) {
            if (queue.isComplete(currentTurn)) {
                if (queue.execute(gameState)) {
                    mapNeedsRefresh = true;
                }
                toRemove.add(queue);
                empire.recalculateProduction(gameState.getWorldState().getMap());
            }
        }
        empire.getQueues().removeAll(toRemove);

        if (mapNeedsRefresh) {
            gamePanel.getMapPanel().markStaticDirty();
            gamePanel.getMapPanel().repaint();
        }
    }

    private void endTurnFor(EmpireState empire) {
        resetProductionForNewTurn(empire);
        resetUnitsForNewTurn(empire);
        applyStoredProduction(empire);
        processStarvation(empire);
        boolean anyDamagesToBuilding = processUpkeep(empire);
        resetBazaars(empire);
        empire.setUpkeepDisaster(anyDamagesToBuilding);
        empire.updateTownCapacity();
    }

    private void worldTick() {
        disasterHandler.handle();
        processTribes();
        spawnWarriorAllyHelpers();
        resetTradingCenters();
        advanceWorld();
    }

    private void resetProductionForNewTurn(EmpireState empire) {
        for (Building building : empire.getBuildings()) {
            building.setProducingStopped(false);
        }
        empire.recalculateProduction(gameState.getWorldState().getMap());
    }

    private void resetUnitsForNewTurn(EmpireState empire) {
        for (Unit unit : empire.getUnits()) {
            unit.resetActionPointsForNewTurn();
            if (empire.getHappiness() <= -5) {
                unit.setActionPoints(unit.getActionPoints() - 1);
            }
        }
    }

    private void applyStoredProduction(EmpireState empire) {
        empire.getStorage().applyProduction(empire.getProduction());
    }

    private void processStarvation(EmpireState empire) {
        if (empire.getStorage().getFoodQuantity() == 0 && empire.getProduction().getFoodProduction() < 0) {
            for (Unit unit : empire.getUnits()) {
                unit.setActionPoints(Math.max(0, unit.getActionPoints() * 2 / 3));
            }
            for (Queue queue : empire.getQueues()) {
                queue.addDelay(1);
            }
            empire.setStarving(true);
        } else {
            empire.setStarving(false);
        }
    }

    private boolean processUpkeep(EmpireState empire) {
        boolean anyDamagesToBuilding = false;
        for (Building building : empire.getBuildings()) {
            if (building.getUpkeep() != null && !building.getUpkeep().enoughResources(empire.getStorage())) {
                Random random = gameState.getRng();
                boolean damageBuilding = random.nextDouble() <= UPKEEP_DAMAGE_CHANCE;
                if (damageBuilding) {
                    anyDamagesToBuilding = true;
                    building.setNoUpkeepTurns(building.getNoUpkeepTurns() + 1);
                    if (building.getNoUpkeepTurns() == MAX_UPKEEP_TURNS) {
                        empire.removeBuilding(building);
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

    private void spawnWarriorAllyHelpers() {
        if (gameState.getWorldState().getTurnNumber() % 3 != 0) return;
        for (java.util.Map.Entry<UUID, EmpireState> entry : gameState.getEmpireStates().entrySet()) {
            EmpireState empire = entry.getValue();
            boolean warriorAlly = empire.getAllies().stream().anyMatch(WarriorTribe.class::isInstance);
            if (!warriorAlly) continue;
            Hex townHallHex = empire.getTownHallHex();
            if (townHallHex == null) continue;
            Swordsman helper = new Swordsman();
            helper.setOwnerID(entry.getKey());
            helper.setHex(townHallHex);
            townHallHex.getUnits().add(helper);
            empire.getUnits().add(helper);
        }
    }

    private void resetTradingCenters() {
        for (TradingCenter tradingCenter : gameState.getWorldState().getTradingCenters()) {
            tradingCenter.setUsedThisTurn(false);
        }
    }

    private void resetBazaars(EmpireState empire) {
        for (Building building : empire.getBuildings()) {
            if (building instanceof Bazaar) {
                Bazaar bazaar = (Bazaar) building;
                bazaar.setUsedThisTurn(false);
            }
        }
    }

    private void advanceWorld() {
        gameState.getWorldState().nextTurn();
        Season.SeasonType old = gameState.getWorldState().getSeason();
        Season.processSeason(gameState);
        Season.SeasonType newSeason = gameState.getWorldState().getSeason();
        for (EmpireState empire : gameState.getEmpireStates().values()) {
            empire.getProduction().setSeason(newSeason);
        }
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
