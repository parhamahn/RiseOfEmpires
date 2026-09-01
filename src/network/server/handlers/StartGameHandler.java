package network.server.handlers;

import model.map.Map;
import model.map.MapCatalog;
import model.product.Production;
import model.state.GameState;
import model.state.Lobby;
import model.storage.Storage;
import network.request.StartGameRequest;
import network.response.ActionResultResponse;
import network.server.GameServer;
import network.server.MessageHandler;
import network.server.Messenger;
import network.server.StateBroadcaster;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StartGameHandler implements MessageHandler<StartGameRequest> {

    private final GameServer gameServer;

    public StartGameHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void handle(UUID senderID, StartGameRequest message) {
        Lobby lobby = gameServer.getLobby();
        Messenger messenger = gameServer.getMessenger();

        if (!senderID.equals(lobby.getHostId())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Only the host can start the game"));
            return;
        }
        if (lobby.getPlayers().isEmpty()) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "No players in the lobby"));
            return;
        }
        if (lobby.getPlayers().values().stream().anyMatch(p -> !p.isReady())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Not all players are ready"));
            return;
        }

        try {
            Map map = MapCatalog.load(lobby.getSelectedMapIndex());
            GameState gameState = new GameState(map);

            List<UUID> order = new ArrayList<>(lobby.getPlayers().keySet());
            for (UUID playerId : order) {
                gameState.addPlayer(playerId, new Storage(), new Production());
            }
            gameState.startMatch(map, order);

            gameServer.setGameState(gameState);
            lobby.setStarted(true);

            StateBroadcaster.broadcast(gameState, order, messenger);
        } catch (Exception e) {
            e.printStackTrace();
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Failed to start game: " + e.getMessage()));
        }
    }
}