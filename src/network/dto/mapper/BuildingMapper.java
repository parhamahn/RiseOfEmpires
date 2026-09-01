package network.dto.mapper;

import model.building.Building;
import model.building.ProducingBuilding;
import model.building.TradingBuilding;
import model.building.empire.*;
import model.building.other.TradingCenter;
import model.map.Hex;
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

    public Building fromDTO(BuildingDTO dto, Hex hex) {
        Hex.ResourceType res = dto.resourceType != null ? Hex.ResourceType.valueOf(dto.resourceType) : null;
        Building building = switch (dto.type) {
            case "TownHall" -> new TownHall(hex);
            case "Farm" -> new Farm(hex, res != null ? res : Hex.ResourceType.WHEAT);
            case "LumberMill" -> new LumberMill(hex);
            case "StoneMine" -> new StoneMine(hex);
            case "IronMine" -> new IronMine(hex);
            case "Stable" -> new Stable(hex, res != null ? res : Hex.ResourceType.COW);
            case "Town" -> new Town(hex);
            case "Monument" -> new Monument(hex);
            case "Bazaar" -> new Bazaar(hex);
            case "Dock" -> new Dock(hex);
            case "MilitaryStable" -> new MilitaryStable(hex);
            case "Apothecary" -> new Apothecary(hex);
            case "TradingCenter" -> new TradingCenter(hex);
            default -> null;
        };
        if (building == null) return null;
        building.setHP(dto.hp);
        building.setMaxHP(dto.maxHp);
        building.setProducingStopped(dto.producingStopped);
        building.setNoUpkeepTurns(dto.noUpkeepTurns);
        if (dto.ownerId != null) building.setOwnerID(java.util.UUID.fromString(dto.ownerId));
        if (building instanceof TradingBuilding trading && dto.usedThisTurn != null) {
            trading.setUsedThisTurn(dto.usedThisTurn);
        }
        hex.setBuilding(building);
        return building;
    }

}
