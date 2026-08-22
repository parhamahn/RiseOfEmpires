package network.dto.mapper;

import model.building.tribes.Tribe;
import network.dto.DtoMapper;
import network.dto.TribeDTO;

public enum TribeMapper implements DtoMapper<Tribe, TribeDTO> {
    INSTANCE;

    @Override
    public TribeDTO toDTO(Tribe tribe) {
        TribeDTO dto = new TribeDTO();
        dto.id = tribe.getId().toString();
        dto.type = tribe.getClass().getSimpleName();
        dto.hexC = tribe.getHex().getC();
        dto.hexR = tribe.getHex().getR();
        dto.name = tribe.getName();
        dto.hp = tribe.getHP();
        dto.maxHp = tribe.getMaxHP();
        dto.relation = tribe.getRelation();
        dto.relationStatus = tribe.getRelationStatus().name();
        dto.discovered = tribe.isDiscovered();
        dto.permanentReward = tribe.isPermanentReward();
        dto.usedThisTurn = tribe.isUsedThisTurn();
        dto.allianceLockTurns = tribe.getAllianceLockTurns();
        dto.lastMissionTurn = tribe.getLastMissionTurn();
        if (tribe.getActiveMission() != null) {
            dto.activeMission = MissionMapper.INSTANCE.toDTO(tribe.getActiveMission());
        }
        dto.tradeRate = tribe.getTradeStrategy() != null ? tribe.getTradeStrategy().getRate() : 0;
        return dto;
    }
}
