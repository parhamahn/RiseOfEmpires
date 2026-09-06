package network.server.handlers;

import model.Player;
import model.state.GameState;
import network.notif.ChatMessageNotif;
import network.request.ChatMessageRequest;
import network.server.GameServer;
import network.server.MessageHandler;
import network.server.Messenger;

import java.util.UUID;

public class ChatMessageHandler implements MessageHandler<ChatMessageRequest> {

    private final GameServer gameServer;

    public ChatMessageHandler(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void handle(UUID senderID, ChatMessageRequest req) {
        GameState gameState = gameServer.getGameState();
        Messenger udpMessenger = gameServer.getUdpMessenger();
        if (udpMessenger == null) return;

        String senderName = "Unknown";
        if (gameState != null) {
            senderName = gameState.getPlayerName(senderID);
        } else if (gameServer.getLobby() != null) {
            Player player = gameServer.getLobby().getPlayers().get(senderID);
            if (player != null) senderName = player.getName();
        }

        udpMessenger.broadcast(new ChatMessageNotif(null, senderID, senderName, req.getContent()));
    }
}