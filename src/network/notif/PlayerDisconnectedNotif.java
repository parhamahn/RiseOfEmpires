package network.notif;

import network.common.MessageType;
import network.common.Notif;

import java.util.UUID;

public class PlayerDisconnectedNotif extends Notif {

    private UUID disconnectedPlayerID;
    private String playerName;

    public PlayerDisconnectedNotif() {
        super();
    }

    public PlayerDisconnectedNotif(UUID senderID, UUID disconnectedPlayerID, String playerName) {
        super(MessageType.PLAYER_DISCONNECTED_NOTIF, senderID, System.currentTimeMillis());
        this.disconnectedPlayerID = disconnectedPlayerID;
        this.playerName = playerName;
    }

    public UUID getDisconnectedPlayerID() { return disconnectedPlayerID; }
    public String getPlayerName() { return playerName; }
}
