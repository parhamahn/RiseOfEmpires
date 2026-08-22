package network.dto.mapper;

import model.building.Building;
import model.building.ProducingBuilding;
import model.building.TradingBuilding;
import network.dto.DtoMapper;
import network.dto.BuildingDTO;

public enum BuildingMapper implements DtoMapper<Building, BuildingDTO> {
    INSTANCE;

    @Override
    public BuildingDTO toDTO(Building building) {
        BuildingDTO dto = new BuildingDTO();
        dto.id = building.getId().toString();
        dto.type = building.getClass().getSimpleName();
        dto.ownerId = building.getOwnerID() != null ? building.getOwnerID().toString() : null;
        if (building.getHex() != null) {
            dto.hexC = building.getHex().getC();
            dto.hexR = building.getHex().getR();
        }
        dto.hp = building.getHP();
        dto.maxHp = building.getMaxHP();
        dto.producingStopped = building.isProducingStopped();
        dto.noUpkeepTurns = building.getNoUpkeepTurns();
        if (building instanceof ProducingBuilding producing) {
            for (model.unit.civilian.Worker worker : producing.getAssignedWorkers()) {
                dto.assignedWorkerIds.add(worker.getId().toString());
            }
            if (producing.getResourceType() != null) {
                dto.resourceType = producing.getResourceType().name();
            }
        }
        if (building instanceof TradingBuilding trading) {
            dto.usedThisTurn = trading.isUsedThisTurn();
        }
        return dto;
    }
}
