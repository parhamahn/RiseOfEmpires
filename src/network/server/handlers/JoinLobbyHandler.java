package network.server.handlers;

import model.Player;
import model.state.Lobby;
import network.request.JoinLobbyRequest;
import network.server.MessageHandler;
import network.server.Messenger;

import java.util.UUID;

public class JoinLobbyHandler implements MessageHandler<JoinLobbyRequest> {
    private final Lobby lobby;
    private final Messenger messenger;
    public JoinLobbyHandler(Lobby lobby, Messenger messenger) { this.lobby = lobby; this.messenger = messenger; }

    public void handle(UUID senderID, JoinLobbyRequest req) {
        Player player = lobby.getPlayers().computeIfAbsent(senderID, id -> new Player(id, req.getPlayerName()));
        if (lobby.getHostId() == null) lobby.setHostId(senderID);

    }
}