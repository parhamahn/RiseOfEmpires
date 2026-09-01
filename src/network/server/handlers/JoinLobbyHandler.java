package network.server.handlers;

import model.Player;
import model.state.Lobby;
import network.notif.LobbyStateNotif;
import network.request.JoinLobbyRequest;
import network.response.ActionResultResponse;
import network.server.GameServer;
import network.server.MessageHandler;
import network.server.Messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinLobbyHandler implements MessageHandler<JoinLobbyRequest> {

    private final GameServer gameServer;

    public JoinLobbyHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void handle(UUID senderID, JoinLobbyRequest req) {
        Lobby lobby = gameServer.getLobby();
        Messenger messenger = gameServer.getMessenger();

        if (lobby.isStarted()) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "The game has already started"));
            return;
        }

        lobby.getPlayers().computeIfAbsent(senderID, id -> new Player(id, req.getPlayerName()));
        if (lobby.getHostId() == null) {
            lobby.setHostId(senderID);
        }

        broadcastLobbyState(gameServer);
    }

    public static void broadcastLobbyState(GameServer gameServer) {
        Lobby lobby = gameServer.getLobby();
        Messenger messenger = gameServer.getMessenger();
        if (lobby == null || messenger == null) return;

        List<LobbyStateNotif.LobbyPlayerInfo> infos = new ArrayList<>();
        for (Player p : lobby.getPlayers().values()) {
            infos.add(new LobbyStateNotif.LobbyPlayerInfo(p.getId(), p.getName(), p.isReady()));
        }
        messenger.broadcast(new LobbyStateNotif(null, infos, lobby.getHostId(), lobby.getSelectedMapIndex()));
    }
}