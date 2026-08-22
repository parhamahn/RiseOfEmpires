package network.dto.mapper;

import model.queue.CookQueue;
import model.queue.Queue;
import model.queue.ResearchQueue;
import model.queue.UpgradeQueue;
import network.dto.DtoMapper;
import network.dto.QueueDTO;

public enum QueueMapper implements DtoMapper<Queue, QueueDTO> {
    INSTANCE;

    @Override
    public QueueDTO toDTO(Queue queue) {
        QueueDTO dto = new QueueDTO();
        dto.queueType = queue.getQueueType().name();
        dto.description = queue.getDescription();
        dto.turnCost = queue.getTurnCost();
        dto.startTurn = queue.getStartTurn();
        if (queue instanceof CookQueue cookQueue) {
            dto.cookingUnitType = cookQueue.getUnit().getUnitType().name();
        } else if (queue instanceof ResearchQueue researchQueue) {
            dto.technology = researchQueue.getTechnology().name();
        } else if (queue instanceof UpgradeQueue upgradeQueue) {
            dto.targetLevel = upgradeQueue.getTargetLevel().name();
        }
        return dto;
    }
}
