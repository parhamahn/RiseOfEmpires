package network.dto.mapper;

import model.state.EmpireState;
import model.building.tribes.Tribe;
import model.queue.Queue;
import model.queue.Technology;
import model.map.Hex;
import network.dto.DtoMapper;
import network.dto.EmpireStateDTO;

public enum EmpireStateMapper implements DtoMapper<EmpireState, EmpireStateDTO> {
    INSTANCE;

    @Override
    public EmpireStateDTO toDTO(EmpireState empireState) {
        return toDTO(empireState, null);
    }

    public EmpireStateDTO toDTO(EmpireState empireState, String playerId) {
        EmpireStateDTO dto = new EmpireStateDTO();
        dto.playerId = playerId;
        dto.storage = StorageMapper.INSTANCE.toDTO(empireState.getStorage());
        dto.production = ProductionMapper.INSTANCE.toDTO(empireState.getProduction());
        dto.maxUnit = empireState.getMaxUnit();
        dto.starving = empireState.isStarving();
        dto.upkeepDisaster = empireState.isUpkeepDisaster();

        for (Hex townHallHex : empireState.getTownHallHexes()) {
            dto.townHallHexes.add(new int[]{townHallHex.getC(), townHallHex.getR()});
        }

        for (Queue queue : empireState.getQueues()) {
            dto.queues.add(QueueMapper.INSTANCE.toDTO(queue));
        }
        for (Technology technology : empireState.getResearchedTechnologies()) {
            dto.researchedTechnologies.add(technology.name());
        }

        dto.townHallLevel = empireState.getTownHallLevel().name();
        dto.happiness = empireState.getHappiness();
        dto.season = empireState.getSeason().name();
        dto.dockDiscount = empireState.isDockDiscount();

        for (Tribe ally : empireState.getAllies()) {
            dto.allyTribeIds.add(ally.getId().toString());
        }
        return dto;
    }
}
