package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class JoinLobbyRequest extends Request {

    private String playerName;

    public JoinLobbyRequest() {
        super();
    }

    public JoinLobbyRequest(UUID senderID, String playerName) {
        super(MessageType.JOIN_LOBBY_REQUEST, senderID, System.currentTimeMillis());
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
