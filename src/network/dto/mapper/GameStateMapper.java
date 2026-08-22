package network.dto.mapper;

import model.building.Building;
import model.building.tribes.Tribe;
import model.state.GameState;
import model.state.EmpireState;
import model.unit.Unit;
import network.dto.DtoMapper;
import network.dto.GameStateDTO;

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
}
