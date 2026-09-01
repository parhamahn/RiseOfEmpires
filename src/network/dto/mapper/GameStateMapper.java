package network.dto.mapper;

import model.Season;
import model.TownHallLevel;
import model.building.Building;
import model.building.ProducingBuilding;
import model.building.other.TradingCenter;
import model.building.tribes.Tribe;
import model.map.Hex;
import model.map.Map;
import model.product.Production;
import model.queue.Technology;
import model.state.GameState;
import model.state.EmpireState;
import model.state.WorldState;
import model.storage.Storage;
import model.unit.Unit;
import model.unit.civilian.Worker;
import network.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public enum GameStateMapper implements DtoMapper<GameState, GameStateDTO> {
    INSTANCE;

    @Override
    public GameStateDTO toDTO(GameState gameState) {
        EmpireState empireState = gameState.getEmpireState();
        int turnNumber = gameState.getWorldState().getTurnNumber();

        GameStateDTO dto = new GameStateDTO();
        dto.turnNumber = turnNumber;
        dto.map = MapMapper.INSTANCE.toDTO(gameState.getWorldState().getMap());

        for (Unit unit : empireState.getUnits()) {
            dto.units.add(UnitMapper.INSTANCE.toDTO(unit));
        }
        for (Unit enemy : gameState.getWorldState().getEnemies()) {
            dto.units.add(UnitMapper.INSTANCE.toDTO(enemy));
        }

        for (Building building : empireState.getBuildings()) {
            dto.buildings.add(BuildingMapper.INSTANCE.toDTO(building));
        }
        for (Building tradingCenter : gameState.getWorldState().getTradingCenters()) {
            dto.buildings.add(BuildingMapper.INSTANCE.toDTO(tradingCenter));
        }

        for (Tribe tribe : gameState.getWorldState().getTribes()) {
            dto.tribes.add(TribeMapper.INSTANCE.toDTO(tribe));
        }

        dto.empires.add(EmpireStateMapper.INSTANCE.toDTO(empireState, "player-1"));
        return dto;
    }

    public GameState fromDTO(GameStateDTO dto) {
        UUID playerId = UUID.randomUUID();

        Map map = MapMapper.INSTANCE.fromDTO(dto.map, playerId);

        java.util.Map<String, Tribe> tribesById = new HashMap<>();
        for (TribeDTO tribeDTO : dto.tribes) {
            Hex hex = map.getHex(tribeDTO.hexC, tribeDTO.hexR);
            if (hex == null) continue;
            Tribe tribe = TribeMapper.INSTANCE.fromDTO(tribeDTO, hex);
            if (tribe != null) tribesById.put(tribeDTO.id, tribe);
        }

        java.util.Map<String, ProducingBuilding> producingById = new HashMap<>();
        java.util.List<Building> playerBuildings = new ArrayList<>();
        java.util.List<TradingCenter> tradingCenters = new ArrayList<>();
        for (BuildingDTO buildingDTO : dto.buildings) {
            Hex hex = map.getHex(buildingDTO.hexC, buildingDTO.hexR);
            if (hex == null || hex.getBuilding() != null) continue;
            Building building = BuildingMapper.INSTANCE.fromDTO(buildingDTO, hex);
            if (building == null) continue;
            if (building instanceof ProducingBuilding pb) producingById.put(buildingDTO.id, pb);
            if (building instanceof TradingCenter tc) tradingCenters.add(tc);
            else playerBuildings.add(building);
        }

        java.util.List<Unit> playerUnits = new ArrayList<>();
        java.util.List<Unit> enemies = new ArrayList<>();
        java.util.Map<String, Unit> unitsById = new HashMap<>();
        for (UnitDTO unitDTO : dto.units) {
            Unit unit = UnitMapper.INSTANCE.fromDTO(unitDTO, tribesById);
            Hex hex = map.getHex(unitDTO.hexC, unitDTO.hexR);
            if (hex != null) {
                unit.setHex(hex);
                hex.getUnits().add(unit);
            }
            if (unitDTO.targetHexC != null && unitDTO.targetHexR != null) {
                unit.setTargetHex(map.getHex(unitDTO.targetHexC, unitDTO.targetHexR));
            }
            unitsById.put(unitDTO.id, unit);
            (unitDTO.ownerId != null ? playerUnits : enemies).add(unit);
        }

        for (BuildingDTO buildingDTO : dto.buildings) {
            ProducingBuilding producing = producingById.get(buildingDTO.id);
            if (producing == null || buildingDTO.assignedWorkerIds == null) continue;
            for (String workerId : buildingDTO.assignedWorkerIds) {
                if (unitsById.get(workerId) instanceof Worker worker) {
                    producing.getAssignedWorkers().add(worker);
                }
            }
        }

        EmpireStateDTO empireDTO = dto.empires.isEmpty() ? null : dto.empires.get(0);
        Storage storage = empireDTO != null ? StorageMapper.INSTANCE.fromDTO(empireDTO.storage) : new Storage();
        EmpireState loadedEmpire = new EmpireState(storage, new Production());

        if (empireDTO != null) {
            loadedEmpire.getBuildings().addAll(playerBuildings);
            loadedEmpire.getUnits().addAll(playerUnits);
            for (int[] coords : empireDTO.townHallHexes) {
                Hex h = map.getHex(coords[0], coords[1]);
                if (h != null) loadedEmpire.addTownHall(h);
            }
            loadedEmpire.setTownHallLevel(TownHallLevel.valueOf(empireDTO.townHallLevel));
            loadedEmpire.setMaxUnit(empireDTO.maxUnit);
            loadedEmpire.setStarving(empireDTO.starving);
            loadedEmpire.setUpkeepDisaster(empireDTO.upkeepDisaster);
            loadedEmpire.setDockDiscount(empireDTO.dockDiscount);
            loadedEmpire.addHappiness(empireDTO.happinessBase);

            for (String tech : empireDTO.researchedTechnologies) {
                loadedEmpire.getResearchedTechnologies().add(Technology.valueOf(tech));
            }
            for (QueueDTO queueDTO : empireDTO.queues) {
                loadedEmpire.getQueues().add(QueueMapper.INSTANCE.fromDTO(queueDTO, loadedEmpire, map));
            }
            for (String allyId : empireDTO.allyTribeIds) {
                Tribe ally = tribesById.get(allyId);
                if (ally != null) loadedEmpire.getAllies().add(ally);
            }
            loadedEmpire.recalculateProduction(map);
        }

        WorldState loadedWorld = new WorldState(map);
        for (int i = 1; i < dto.turnNumber; i++) loadedWorld.nextTurn();
        loadedWorld.getTribes().addAll(tribesById.values());
        loadedWorld.getTradingCenters().addAll(tradingCenters);
        loadedWorld.getEnemies().addAll(enemies);

        GameState gameState = new GameState(new Storage(), new Production());
        UUID throwawayId = gameState.getLocalPlayerId();
        gameState.getEmpireStates().remove(throwawayId);
        gameState.getTurnOrder().remove(throwawayId);

        gameState.getWorldState().loadFrom(loadedWorld);
        EmpireState realEmpire = gameState.addPlayer(playerId, storage, loadedEmpire.getProduction());
        realEmpire.loadFrom(loadedEmpire);
        gameState.setLocalPlayerId(playerId);

        Season.processSeason(gameState);

        return gameState;
    }
}
