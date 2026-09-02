package network.server.handlers;

import model.map.Hex;
import model.map.Map;
import model.state.EmpireState;
import model.state.GameState;
import model.unit.Unit;
import network.request.MoveUnitRequest;
import network.response.ActionResultResponse;
import network.server.GameServer;
import network.server.MessageHandler;
import network.server.Messenger;
import network.server.StateBroadcaster;

import java.util.UUID;

public class MoveUnitHandler implements MessageHandler<MoveUnitRequest> {

    private final GameServer gameServer;

    public MoveUnitHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void handle(UUID senderID, MoveUnitRequest req) {
        GameState gameState = gameServer.getGameState();
        Messenger messenger = gameServer.getMessenger();
        if (gameState == null) return;

        if (!senderID.equals(gameState.getCurrentPlayerId())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "It's not your turn"));
            return;
        }

        EmpireState empire = gameState.getEmpireState(senderID);
        if (empire == null) return;

        Unit unit = empire.getUnits().stream()
                .filter(u -> u.getId().equals(req.getUnitId()))
                .findFirst().orElse(null);

        if (unit == null) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Unit not found or not yours"));
            return;
        }

        Hex fromHex = unit.getHex();
        Map map = gameState.getWorldState().getMap();
        Hex toHex = map.getHex(req.getToCol(), req.getToRow());

        if (fromHex == null || toHex == null) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Invalid destination"));
            return;
        }
        if (!map.getNeighbourHexes(fromHex).contains(toHex)) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Destination is not adjacent"));
            return;
        }
        if (toHex.getTerrainType() == Hex.TerrainType.SEA
                && !empire.getResearchedTechnologies().contains(model.queue.Technology.SAILING)) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Sailing not researched"));
            return;
        }

        if (toHex.getTerrainType()== Hex.TerrainType.TALL_MOUNTAIN){
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Can't move to a tall mountain"));
        }

        if (!toHex.canAddUnitType(unit.getUnitType())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Destination hex is full"));
            return;
        }

        int apNeeded = fromHex.getMoveAPCostTo(toHex, gameState.getWorldState().getSeason());
        if (unit.getActionPoints() < apNeeded) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Not enough action points"));
            return;
        }

        if (fromHex.getBuilding() instanceof model.building.ProducingBuilding producing) {
            producing.getAssignedWorkers().remove(unit);
            empire.recalculateProduction(map);
        }

        unit.setActionPoints(unit.getActionPoints() - apNeeded);
        fromHex.getUnits().remove(unit);
        toHex.getUnits().add(unit);
        unit.setHex(toHex);
        unit.setAssigned(false);

        messenger.sendTo(senderID, new ActionResultResponse(null, true, "Unit moved"));
        StateBroadcaster.broadcast(gameState, gameState.getTurnOrder(), messenger);
    }
}