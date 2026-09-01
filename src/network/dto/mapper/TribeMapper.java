package network.dto.mapper;

import model.building.tribes.*;
import model.map.Hex;
import model.mission.Mission;
import model.mission.MissionStatus;
import model.mission.WarriorMission;
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

    public Tribe fromDTO(TribeDTO dto, Hex hex) {
        Tribe tribe = switch (dto.type) {
            case "FarmerTribe" -> new FarmerTribe(hex);
            case "MountainTribe" -> new MountainTribe(hex);
            case "BeachTribe" -> new BeachTribe(hex);
            case "TraderTribe" -> new TraderTribe(hex);
            case "WarriorTribe" -> new WarriorTribe(hex);
            default -> null;
        };
        if (tribe == null) return null;
        tribe.setHP(dto.hp);
        tribe.setMaxHP(dto.maxHp);
        tribe.setName(dto.name);
        tribe.setRelation(dto.relation);
        tribe.setRelationStatus(RelationStatus.valueOf(dto.relationStatus));
        tribe.setDiscovered(dto.discovered);
        tribe.setPermanentReward(dto.permanentReward);
        tribe.setUsedThisTurn(dto.usedThisTurn);
        tribe.setAllianceLockTurns(dto.allianceLockTurns);
        tribe.setLastMissionTurn(dto.lastMissionTurn);
        if (dto.activeMission != null && !tribe.getMissions().isEmpty()) {
            Mission mission = tribe.getMissions().get(0);
            mission.setStatus(MissionStatus.valueOf(dto.activeMission.status));
            mission.resetDeadline();
            int toBurn = mission.getDeadline() - dto.activeMission.deadline;
            for (int i = 0; i < toBurn; i++) mission.decreaseDeadline();
            if (mission instanceof WarriorMission warriorMission && dto.activeMission.killedEnemies != null) {
                for (int i = 0; i < dto.activeMission.killedEnemies; i++) warriorMission.enemyKilled();
            }
            tribe.setActiveMission(mission);
        }
        hex.setBuilding(tribe);
        return tribe;
    }

}
