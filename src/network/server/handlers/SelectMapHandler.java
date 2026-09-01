package network.server.handlers;

import model.state.Lobby;
import network.request.SelectMapRequest;
import network.response.ActionResultResponse;
import network.server.GameServer;
import network.server.MessageHandler;
import network.server.Messenger;

import java.util.UUID;

public class SelectMapHandler implements MessageHandler<SelectMapRequest> {

    private final GameServer gameServer;

    public SelectMapHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void handle(UUID senderID, SelectMapRequest message) {
        Lobby lobby = gameServer.getLobby();
        Messenger messenger = gameServer.getMessenger();

        if (!senderID.equals(lobby.getHostId())) {
            messenger.sendTo(senderID, new ActionResultResponse(null, false, "Only the host can select the map"));
            return;
        }

        lobby.setSelectedMapIndex(message.getMapIndex());
        JoinLobbyHandler.broadcastLobbyState(gameServer);
    }
}