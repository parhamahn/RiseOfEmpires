package network.server.handlers;

import model.Task;
import model.building.Building;
import model.building.BuildingFactory;
import model.building.empire.Dock;
import model.map.Hex;
import model.map.Map;
import model.state.EmpireState;
import model.state.GameState;
import model.storage.Cost;
import model.unit.Unit;
import network.request.BuildRequest;
import network.response.ActionResultResponse;
import network.server.GameServer;
import network.server.MessageHandler;
import network.server.Messenger;
import network.server.StateBroadcaster;

import java.util.Comparator;
import java.util.UUID;

public class BuildHandler implements MessageHandler<BuildRequest> {

    private final GameServer gameServer;

    public BuildHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void handle(UUID senderID, BuildRequest message) {
        GameState gameState = gameServer.getGameState();
        Messenger messenger = gameServer.getMessenger();
        if (gameState == null) return;

        if (!senderID.equals(gameState.getCurrentPlayerId())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "It's not your turn"));
            return;
        }

        EmpireState empireState = gameState.getEmpireState(senderID);
        if (empireState == null) return;

        Task.TASK task = message.getTaskType();

        Map map = gameState.getWorldState().getMap();
        Hex hex = map.getHex(message.getCol(), message.getRow());
        if (hex == null) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Invalid hex"));
            return;
        }
        if (!hex.isDiscoveredBy(senderID)) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Hex is not discovered"));
            return;
        }
        if (hex.getBuilding() != null) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Hex is already occupied"));
            return;
        }
        if (!hex.isOwnedBy(senderID)) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Hex is not within your empire border"));
            return;
        }
        if (!Task.getAvailableTasks(hex, gameState).contains(task)) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "This building can't be placed here"));
            return;
        }

        String blockReason = Task.getBuildBlockReason(task, hex, gameState);
        if (blockReason != null) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, blockReason));
            return;
        }

        Building building = BuildingFactory.createBuilding(task, hex);
        building.setOwnerID(senderID);

        Cost cost = building.getCost();
        if (building instanceof Dock && empireState.isDockDiscount()) {
            empireState.setDockDiscount(false);
            cost = new Cost(cost.getWoodNeeded() / 2, cost.getStoneNeeded() / 2,
                    cost.getIronNeeded() / 2, cost.getFoodNeeded() / 2, cost.getAPNeeded());
        }

        if (!cost.enoughResources(empireState.getStorage())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false,
                    "Not enough " + cost.missingResources(empireState.getStorage())));
            return;
        }
        if (!hasBuilderWithAP(hex, cost.getAPNeeded())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false,
                    "No builder in this hex has enough action points"));
            return;
        }

        deductBuilderCost(cost, hex, empireState);
        hex.setBuilding(building);
        empireState.getBuildings().add(building);
        if (building.isTown()) {
            empireState.addHappiness(-1);
        }
        empireState.getBuildings().sort(Comparator.comparingInt(b -> b.getHex().getR()));

        empireState.updateTownCapacity();
        empireState.recalculateProduction(map);

        StateBroadcaster.broadcast(gameState, gameState.getTurnOrder(), messenger);
    }

    private boolean hasBuilderWithAP(Hex hex, int apNeeded) {
        return hex.getUnits().stream().anyMatch(unit -> unit.isBuilder() && unit.getActionPoints() >= apNeeded);
    }

    private void deductBuilderCost(Cost cost, Hex hex, EmpireState empireState) {
        cost.pay(empireState.getStorage());
        for (Unit unit : hex.getUnits()) {
            if (unit.isBuilder() && unit.getActionPoints() >= cost.getAPNeeded()) {
                unit.setActionPoints(unit.getActionPoints() - cost.getAPNeeded());
                unit.setCharges(unit.getCharges() - 1);
                if (unit.getCharges() == 0) {
                    empireState.removeUnit(unit);
                }
                break;
            }
        }
    }
}