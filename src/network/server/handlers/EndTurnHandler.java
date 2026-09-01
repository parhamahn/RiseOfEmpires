package network.server.handlers;

import model.engine.TurnEngine;
import model.state.EmpireState;
import model.state.GameState;
import model.unit.Unit;
import network.request.EndTurnRequest;
import network.response.ActionResultResponse;
import network.server.GameServer;
import network.server.MessageHandler;
import network.server.Messenger;
import network.server.StateBroadcaster;

import java.util.UUID;

public class EndTurnHandler implements MessageHandler<EndTurnRequest> {

    private final GameServer gameServer;

    public EndTurnHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void handle(UUID senderID, EndTurnRequest message) {
        GameState gameState = gameServer.getGameState();
        Messenger messenger = gameServer.getMessenger();
        if (gameState == null) return;

        if (!senderID.equals(gameState.getCurrentPlayerId())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "It's not your turn"));
            return;
        }

        EmpireState actor = gameState.getEmpireState(senderID);
        if (actor == null) return;

        actor.applyTurnEndHappiness();
        for (Unit unit : actor.getUnits()) {
            unit.setUsedItem(false);
        }

        TurnEngine.endTurnFor(gameState, actor);
        if (gameState.isLastInRound()) {
            TurnEngine.rollAndApplyDisasters(gameState);
            TurnEngine.worldTick(gameState);
        }
        TurnEngine.processQueues(gameState, actor);
        TurnEngine.resolveEnemyAttacks(gameState);

        gameState.nextPlayerTurn();

        StateBroadcaster.broadcast(gameState, gameState.getTurnOrder(), messenger);
    }
}