package network.dto.mapper;

import model.building.tribes.Tribe;
import model.unit.Unit;
import model.unit.UnitFactory;
import model.unit.enemy.Guard;
import network.dto.DtoMapper;
import network.dto.UnitDTO;

public enum UnitMapper implements DtoMapper<Unit, UnitDTO> {
    INSTANCE;

    @Override
    public UnitDTO toDTO(Unit unit) {
        UnitDTO dto = new UnitDTO();
        dto.id = unit.getId().toString();
        dto.unitType = unit.getUnitType().name();
        dto.ownerId = unit.getOwnerID() != null ? unit.getOwnerID().toString() : null;
        if (unit.getHex() != null) {
            dto.hexC = unit.getHex().getC();
            dto.hexR = unit.getHex().getR();
        }
        if (unit.getTargetHex() != null) {
            dto.targetHexC = unit.getTargetHex().getC();
            dto.targetHexR = unit.getTargetHex().getR();
        }
        dto.actionPoints = unit.getActionPoints();
        dto.actionPointsMax = unit.getActionPointsMax();
        dto.health = unit.getHealth();
        dto.maxHealth = unit.getMaxHealth();
        dto.hp = unit.getHP();
        dto.maxHp = unit.getMaxHP();
        dto.assigned = unit.isAssigned();
        dto.moving = unit.isMoving();
        dto.charges = unit.getCharges();
        if (unit instanceof Guard guard && guard.getTribe() != null) {
            dto.tribeId = guard.getTribe().getId().toString();
        }
        return dto;
    }

    public Unit fromDTO(UnitDTO dto, java.util.Map<String, Tribe> tribesById) {
        Unit.UNIT_TYPE type = Unit.UNIT_TYPE.valueOf(dto.unitType);
        Unit unit;
        if (type == Unit.UNIT_TYPE.GUARD) {
            Tribe tribe = dto.tribeId != null ? tribesById.get(dto.tribeId) : null;
            unit = new Guard(tribe);
        } else {
            unit = UnitFactory.createUnit(type);
        }
        if (dto.ownerId != null) unit.setOwnerID(java.util.UUID.fromString(dto.ownerId));
        unit.setActionPoints(dto.actionPoints);
        unit.setHP(dto.hp);
        unit.setMaxHP(dto.maxHp);
        unit.setHealth(dto.health);
        unit.setAssigned(dto.assigned);
        unit.setMoving(dto.moving);
        unit.setCharges(dto.charges);
        return unit;
    }

}
