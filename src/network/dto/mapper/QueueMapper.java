package network.dto.mapper;

import model.TownHallLevel;
import model.map.Hex;
import model.map.Map;
import model.queue.*;
import model.state.EmpireState;
import model.unit.Unit;
import model.unit.UnitFactory;
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
        dto.delay = queue.getDelay();
        if (queue instanceof CookQueue cookQueue) {
            dto.cookingUnitType = cookQueue.getUnit().getUnitType().name();
            if (cookQueue.getUnit().getHex() == null) {
                dto.cookHexC = null;
                dto.cookHexR = null;
            }
        } else if (queue instanceof ResearchQueue researchQueue) {
            dto.technology = researchQueue.getTechnology().name();
        } else if (queue instanceof UpgradeQueue upgradeQueue) {
            dto.targetLevel = upgradeQueue.getTargetLevel().name();
        }
        return dto;
    }

    public Queue fromDTO(QueueDTO dto, EmpireState empireState, Map map) {
        Queue queue = switch (Queue.QUEUE_TYPE.valueOf(dto.queueType)) {
            case COOK -> {
                Unit unit = UnitFactory.createUnit(Unit.UNIT_TYPE.valueOf(dto.cookingUnitType));
                Hex target = dto.cookHexC != null && dto.cookHexR != null
                        ? map.getHex(dto.cookHexC, dto.cookHexR)
                        : empireState.getTownHallHex();
                yield new CookQueue(unit, dto.turnCost, dto.startTurn, target);
            }
            case RESEARCH -> new ResearchQueue(Technology.valueOf(dto.technology), dto.turnCost, dto.startTurn);
            case UPGRADE -> new UpgradeQueue(TownHallLevel.valueOf(dto.targetLevel), dto.turnCost, dto.startTurn);
        };
        queue.addDelay(dto.delay);
        return queue;
    }
}
