package model.state;

import model.MapStructure;
import model.Season;
import model.building.Building;
import model.map.Hex;
import model.map.Map;
import model.map.generator.*;
import model.building.tribes.Tribe;
import model.unit.Unit;
import model.unit.enemy.Guard;
import model.product.Production;
import model.storage.Storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameState implements Serializable {
private static final long serialVersionUID = 2L;

    private final java.util.Map<UUID, EmpireState> empireStates = new ConcurrentHashMap<>();
    private final java.util.Map<UUID, String> playerNames = new ConcurrentHashMap<>();
    private final List<UUID> turnOrder = new ArrayList<>();
    private int turnIndex = 0;
    private UUID localPlayerId;
    private transient UUID activeContextId;
    private Diplomacy diplomacy = new Diplomacy();
    private final WorldState worldState;
    private Random rng;

    public GameState(Storage storage, Production production) {
        this.rng = new Random();
        this.worldState = new WorldState(new Map(32, 50, rng));
        EmpireState empireState = addPlayer(UUID.randomUUID(), storage, production);
        empireState.getProduction().setRng(rng);
        restartGame();
    }

    public GameState(Map map) {
        this.rng = new Random();
        this.worldState = new WorldState(map);
        map.setResources(rng);
    }

    public EmpireState addPlayer(UUID playerId, Storage storage, Production production) {
        return addPlayer(playerId, new EmpireState(storage, production));
    }

    public EmpireState addPlayer(UUID playerId, EmpireState empireState) {
        empireStates.put(playerId, empireState);
        turnOrder.add(playerId);
        if (localPlayerId == null) localPlayerId = playerId;
        return empireState;
    }

    public void restartGame() {
        Map map = worldState.getMap();
        List<Hex> spawns = map.getSpawnPoints();
        if (spawns == null || spawns.isEmpty()) {
            Hex center = map.getHex(map.getCols() / 2, map.getRows() / 2);
            spawns = new ArrayList<>();
            for (int i = 0; i < turnOrder.size(); i++) {
                spawns.add(center);
            }
            map.setSpawnPoints(spawns);
        }
        startMatch(map, new ArrayList<>(turnOrder));
    }

    public void startMatch(Map map, List<UUID> playerOrder) {
        List<Hex> spawns = map.getSpawnPoints();
        int spawnCount = spawns == null ? 0 : spawns.size();
        if (playerOrder.size() > spawnCount) {
            throw new IllegalStateException("Selected map provides " + spawnCount
                    + " spawn points for " + playerOrder.size() + " players");
        }
        worldState.reset(map);
        worldState.setSeason(Season.SeasonType.SPRING);
        int index = 0;
        for (UUID playerId : playerOrder) {
            EmpireState empire = empireStates.get(playerId);
            if (empire == null) {
                throw new IllegalStateException("No empire registered for player " + playerId);
            }
            empire.init(map, spawns.get(index), playerId);
            empire.getProduction().setSeason(worldState.getSeason());
            index++;
        }
        turnIndex = 0;
        Generator.generate(this);
    }

    public EmpireState getEmpireState() {
        UUID id = activeContextId != null ? activeContextId : localPlayerId;
        return id != null ? empireStates.get(id) : null;
    }

    public EmpireState getEmpireState(UUID playerId) { return empireStates.get(playerId); }
    public java.util.Map<UUID, EmpireState> getEmpireStates() { return empireStates; }
    public java.util.Map<UUID, String> getPlayerNames() { return playerNames; }
    public String getPlayerName(UUID playerId) { return playerNames.getOrDefault(playerId, "Unknown"); }
    public List<UUID> getTurnOrder() { return turnOrder; }
    public UUID getCurrentPlayerId() { return turnOrder.isEmpty() ? null : turnOrder.get(turnIndex); }
    public void nextPlayerTurn() { if (!turnOrder.isEmpty()) turnIndex = (turnIndex + 1) % turnOrder.size(); }
    public boolean isLastInRound() { return !turnOrder.isEmpty() && turnIndex == turnOrder.size() - 1; }

    public UUID resolveActivePlayerId() { return activeContextId != null ? activeContextId : localPlayerId; }
    public void setActiveContextId(UUID activeContextId) { this.activeContextId = activeContextId; }
    public UUID getActiveContextId() { return activeContextId; }
    public UUID getLocalPlayerId() { return localPlayerId; }
    public void setLocalPlayerId(UUID localPlayerId) { this.localPlayerId = localPlayerId; }

    public Diplomacy getDiplomacy() { return diplomacy; }

    public WorldState getWorldState() { return worldState; }

    public void recordTribeAttack(Hex attackerHex, Hex defenderHex) {
        for (Tribe tribe : worldState.getTribes()) {
            boolean campHit = tribe.getHex() == defenderHex;
            boolean guardHit = defenderHex.getUnits().stream()
                    .anyMatch(unit -> unit instanceof Guard && ((Guard) unit).getTribe() == tribe);
            if (campHit || guardHit) {
                tribe.markAttacked(attackerHex, worldState.getTurnNumber());
            }
        }
    }

    public Random getRng() { return rng; }

    public void removeDeadUnit(Unit unit) {
        if (unit == null || unit.getHex() == null) return;
        EmpireState owner = unit.getOwnerID() != null ? empireStates.get(unit.getOwnerID()) : null;
        if (owner != null && owner.getUnits().contains(unit)) {
            owner.removeUnit(unit);
            return;
        }
        unit.getHex().getUnits().remove(unit);
        worldState.getEnemies().remove(unit);
    }

    public void removeDestroyedStructure(MapStructure structure) {
        if (structure instanceof Building building) {
            if (building.getHex() == null) return;
            EmpireState owner = building.getOwnerID() != null ? empireStates.get(building.getOwnerID()) : null;
            if (owner != null) {
                owner.removeBuilding(building);
                return;
            }
            building.getHex().setBuilding(null);
            worldState.getTradingCenters().remove(building);
        } else if (structure instanceof Tribe tribe) {
            tribe.destroyCamp(this);
        }
    }

    public void reset(){
        Map map = new Map(32, 50, rng);
        for (EmpireState empireState : empireStates.values()) {
            empireState.getStorage().reset();
            empireState.getProduction().reset();
            empireState.reset();
        }
        worldState.reset(map);
        turnIndex = 0;
        restartGame();
    }

    public void loadFrom(GameState loaded) {
        this.rng = loaded.getRng() != null ? loaded.getRng() : new Random();
        this.worldState.loadFrom(loaded.getWorldState());
        this.empireStates.clear();
        this.empireStates.putAll(loaded.getEmpireStates());
        this.playerNames.clear();
        this.playerNames.putAll(loaded.getPlayerNames());
        this.turnOrder.clear();
        this.turnOrder.addAll(loaded.getTurnOrder());
        this.turnIndex = loaded.turnIndex;
        this.diplomacy = loaded.getDiplomacy();
        this.localPlayerId = loaded.getLocalPlayerId();
        this.activeContextId = null;
        for (EmpireState empireState : empireStates.values()) {
            empireState.getProduction().setRng(this.rng);
        }
    }

}
