package network.server.handlers;

import model.Player;
import model.state.Lobby;
import network.request.ReadyToggleRequest;
import network.server.GameServer;
import network.server.MessageHandler;

import java.util.UUID;

public class ReadyToggleHandler implements MessageHandler<ReadyToggleRequest> {

    private final GameServer gameServer;

    public ReadyToggleHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void handle(UUID senderID, ReadyToggleRequest message) {
        Lobby lobby = gameServer.getLobby();
        Player player = lobby.getPlayers().get(senderID);
        if (player == null) return;

        player.setReady(message.isReady());
        JoinLobbyHandler.broadcastLobbyState(gameServer);
    }
}