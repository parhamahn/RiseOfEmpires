package network.dto.mapper;

import model.mission.Mission;
import model.mission.WarriorMission;
import network.dto.DtoMapper;
import network.dto.MissionDTO;

public enum MissionMapper implements DtoMapper<Mission, MissionDTO> {
    INSTANCE;

    @Override
    public MissionDTO toDTO(Mission mission) {
        MissionDTO dto = new MissionDTO();
        dto.status = mission.getStatus().name();
        dto.summary = mission.getSummary();
        dto.deadline = mission.getDeadline();
        if (mission instanceof WarriorMission warriorMission) {
            dto.killedEnemies = warriorMission.getKilledEnemies();
        }
        return dto;
    }
}
