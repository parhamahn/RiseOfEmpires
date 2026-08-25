package model.engine;

import model.Season;
import model.building.Building;
import model.building.empire.Bazaar;
import model.building.other.TradingCenter;
import model.building.tribes.RelationStatus;
import model.building.tribes.Tribe;
import model.building.tribes.WarriorTribe;
import model.combat.AttackUnit;
import model.disasters.BearAttack;
import model.disasters.Earthquake;
import model.disasters.Flood;
import model.disasters.Disaster;
import model.disasters.NaturalDisaster;
import model.disasters.Tsunami;
import model.map.Hex;
import model.map.Map;
import model.map.MapPath;
import model.queue.Queue;
import model.state.EmpireState;
import model.state.GameState;
import model.unit.Unit;
import model.unit.combat.Swordsman;
import model.unit.enemy.Bear;
import model.unit.enemy.Guard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TurnEngine {
    private static final double UPKEEP_DAMAGE_CHANCE = 0.3;
    private static final int MAX_UPKEEP_TURNS = 3;

    private TurnEngine() {
    }

    public static boolean hasUnspentActionPoints(EmpireState empire) {
        for (Unit unit : empire.getUnits()) {
            if (unit.getActionPoints() != 0) {
                return true;
            }
        }
        return false;
    }

    public static List<GameEvent> endTurnFor(GameState gameState, EmpireState empire) {
        List<GameEvent> events = new ArrayList<>();
        resetProductionForNewTurn(gameState, empire);
        resetUnitsForNewTurn(empire);
        applyStoredProduction(empire);
        events.addAll(processStarvation(empire));
        boolean anyDamagesToBuilding = processUpkeep(gameState, empire);
        resetBazaars(empire);
        empire.setUpkeepDisaster(anyDamagesToBuilding);
        empire.updateTownCapacity();
        return events;
    }

    public static List<GameEvent> worldTick(GameState gameState) {
        List<GameEvent> events = new ArrayList<>();
        events.addAll(processTribes(gameState));
        spawnWarriorAllyHelpers(gameState);
        resetTradingCenters(gameState);
        advanceWorld(gameState, events);
        return events;
    }

    public static List<Disaster> rollAndApplyDisasters(GameState gameState) {
        List<NaturalDisaster.DisasterType> disasters = NaturalDisaster.getRandomDisasters(gameState.getRng());
        if (gameState.getWorldState().getSeason() != Season.SeasonType.FALL
                && disasters.contains(NaturalDisaster.DisasterType.FLOOD)) {
            disasters.remove(NaturalDisaster.DisasterType.FLOOD);
        }
        List<Disaster> applied = new ArrayList<>();
        for (NaturalDisaster.DisasterType disasterType : disasters) {
            Disaster disaster = createDisaster(gameState, disasterType);
            if (disaster == null) continue;
            applied.add(disaster);
            disaster.effect(gameState);
        }
        return applied;
    }

    private static Disaster createDisaster(GameState gameState, NaturalDisaster.DisasterType disasterType) {
        Map map = gameState.getWorldState().getMap();
        Random rng = gameState.getRng();
        return switch (disasterType) {
            case EARTHQUAKE -> new Earthquake(map, rng);
            case FLOOD -> new Flood(map, rng);
            case TSUNAMI -> new Tsunami(map, rng);
            case BEAR_ATTACK -> new BearAttack(map, gameState, rng);
        };
    }

    public static List<GameEvent> processQueues(GameState gameState, EmpireState empire) {
        List<GameEvent> events = new ArrayList<>();
        List<Queue> toRemove = new ArrayList<>();
        int currentTurn = gameState.getWorldState().getTurnNumber();

        for (Queue queue : empire.getQueues()) {
            if (queue.isComplete(currentTurn)) {
                queue.execute(gameState);
                toRemove.add(queue);
                empire.recalculateProduction(gameState.getWorldState().getMap());
                events.add(new GameEvent(GameEvent.Type.QUEUE_COMPLETED, queue.getDescription()));
            }
        }
        empire.getQueues().removeAll(toRemove);
        return events;
    }

    private static void resetProductionForNewTurn(GameState gameState, EmpireState empire) {
        for (Building building : empire.getBuildings()) {
            building.setProducingStopped(false);
        }
        empire.recalculateProduction(gameState.getWorldState().getMap());
    }

    private static void resetUnitsForNewTurn(EmpireState empire) {
        for (Unit unit : empire.getUnits()) {
            unit.resetActionPointsForNewTurn();
            if (empire.getHappiness() <= -5) {
                unit.setActionPoints(unit.getActionPoints() - 1);
            }
        }
    }

    private static void applyStoredProduction(EmpireState empire) {
        empire.getStorage().applyProduction(empire.getProduction());
    }

    private static List<GameEvent> processStarvation(EmpireState empire) {
        List<GameEvent> events = new ArrayList<>();
        boolean wasStarving = empire.isStarving();
        if (empire.getStorage().getFoodQuantity() == 0 && empire.getProduction().getFoodProduction() < 0) {
            for (Unit unit : empire.getUnits()) {
                unit.setActionPoints(Math.max(0, unit.getActionPoints() * 2 / 3));
            }
            for (Queue queue : empire.getQueues()) {
                queue.addDelay(1);
            }
            empire.setStarving(true);
            if (!wasStarving) events.add(new GameEvent(GameEvent.Type.STARVATION_STARTED));
        } else {
            empire.setStarving(false);
            if (wasStarving) events.add(new GameEvent(GameEvent.Type.STARVATION_ENDED));
        }
        return events;
    }

    private static boolean processUpkeep(GameState gameState, EmpireState empire) {
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

    public static List<GameEvent> processTribes(GameState gameState) {
        List<GameEvent> events = new ArrayList<>();
        for (Tribe tribe : gameState.getWorldState().getTribes()) {
            tribe.setUsedThisTurn(false);
            if (tribe.getAllianceLockTurns() > 0) {
                tribe.setAllianceLockTurns(tribe.getAllianceLockTurns() - 1);
            }
            if (tribe.tickActiveMission(gameState)) {
                events.add(new GameEvent(GameEvent.Type.MISSION_FAILED, tribe.getName()));
            }
            if (tribe.refreshMissions(gameState.getWorldState().getTurnNumber())) {
                events.add(new GameEvent(GameEvent.Type.NEW_MISSION_AVAILABLE, tribe.getName()));
            }
            if (tribe.getRelationStatus() == RelationStatus.ENEMY) {
                long guards = tribe.getHex().getUnits().stream().filter(Guard.class::isInstance).count();
                int guardCap = tribe instanceof WarriorTribe ? 5 : 3;
                if (guards < guardCap && gameState.getWorldState().getTurnNumber() % 3 == 0) {
                    Guard guard = new Guard(tribe);
                    guard.setHex(tribe.getHex());
                    tribe.getHex().getUnits().add(guard);
                    gameState.getWorldState().getEnemies().add(guard);
                    events.add(new GameEvent(GameEvent.Type.GUARD_SPAWNED, tribe.getName()));
                }
                moveGuardsTowardLastAttacker(gameState, tribe);
            } else {
                List<Unit> tribeGuards = tribe.getHex().getUnits().stream().filter(Guard.class::isInstance).toList();
                for (Unit guard : tribeGuards) {
                    tribe.getHex().getUnits().remove(guard);
                    gameState.getWorldState().getEnemies().remove(guard);
                }
            }
            if (tribe.getRelationStatus() != RelationStatus.ENEMY && isEmpireBuildingNear(gameState, tribe.getHex())) {
                tribe.changeRelation(tribe.getRelation() - 5, gameState.getEmpireState().getAllies());
                if (tribe.getRelationStatus() == RelationStatus.ENEMY) {
                    events.add(new GameEvent(GameEvent.Type.TRIBE_NOW_ENEMY, tribe.getName()));
                }
            }
        }
        return events;
    }

    private static void moveGuardsTowardLastAttacker(GameState gameState, Tribe tribe) {
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

    private static void spawnWarriorAllyHelpers(GameState gameState) {
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

    private static void resetTradingCenters(GameState gameState) {
        for (TradingCenter tradingCenter : gameState.getWorldState().getTradingCenters()) {
            tradingCenter.setUsedThisTurn(false);
        }
    }

    private static void resetBazaars(EmpireState empire) {
        for (Building building : empire.getBuildings()) {
            if (building instanceof Bazaar bazaar) {
                bazaar.setUsedThisTurn(false);
            }
        }
    }

    private static void advanceWorld(GameState gameState, List<GameEvent> events) {
        gameState.getWorldState().nextTurn();
        Season.SeasonType old = gameState.getWorldState().getSeason();
        Season.processSeason(gameState);
        Season.SeasonType newSeason = gameState.getWorldState().getSeason();
        for (EmpireState empire : gameState.getEmpireStates().values()) {
            empire.getProduction().setSeason(newSeason);
        }
        events.add(new GameEvent(GameEvent.Type.SEASON_CHANGED, old + "->" + newSeason));
    }

    private static boolean isEmpireBuildingNear(GameState gameState, Hex tribeHex) {
        for (EmpireState empireState : gameState.getEmpireStates().values()) {
            for (Building building : empireState.getBuildings()) {
                if (gameState.getWorldState().getMap().getDistance(building.getHex(), tribeHex) <= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<GameEvent> resolveEnemyAttacks(GameState gameState) {
        List<GameEvent> events = new ArrayList<>();
        for (Unit enemy : gameState.getWorldState().getEnemies()) {
            if (enemy.getHex() == null || enemy instanceof Bear) continue;
            Hex target = findAdjacentPlayerHex(gameState, enemy.getHex());
            if (target == null) continue;
            if (enemy.getHex().getUnits().stream().noneMatch(u -> u.getUnitType().isEnemy())) continue;
            if (target.getUnits().isEmpty()) continue;

            AttackUnit attackUnit = new AttackUnit(gameState, enemy.getHex(), target, true);
            attackUnit.rollDice();
            attackUnit.setPowers();
            attackUnit.attack();

            if (enemy instanceof Guard guard) {
                events.add(new GameEvent(GameEvent.Type.TRIBE_ATTACKED_PLAYER, guard.getTribe().getName()));
            }
        }
        return events;
    }

    private static Hex findAdjacentPlayerHex(GameState gameState, Hex enemyHex) {
        for (Hex neighbour : gameState.getWorldState().getMap().getNeighbourHexes(enemyHex)) {
            if (neighbour != null && !neighbour.getUnits().isEmpty()
                    && neighbour.getUnits().stream().anyMatch(u -> !u.getUnitType().isEnemy())) {
                return neighbour;
            }
        }
        return null;
    }
}
