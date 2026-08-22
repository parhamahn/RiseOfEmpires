package network.dto.mapper;

import model.unit.Unit;
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
}
