package network.notif;

import network.common.MessageType;
import network.common.Notif;

import java.util.List;
import java.util.UUID;

public class LobbyStateNotif extends Notif {

    private List<LobbyPlayerInfo> players;
    private UUID hostID;
    private int selectedMapIndex;

    public LobbyStateNotif() {
        super();
    }

    public LobbyStateNotif(UUID senderID, List<LobbyPlayerInfo> players, UUID hostID, int selectedMapIndex) {
        super(MessageType.LOBBY_STATE_NOTIF, senderID, System.currentTimeMillis());
        this.players = players;
        this.hostID = hostID;
        this.selectedMapIndex = selectedMapIndex;
    }

    public List<LobbyPlayerInfo> getPlayers() { return players; }
    public UUID getHostID() { return hostID; }
    public int getSelectedMapIndex() { return selectedMapIndex; }

    public static class LobbyPlayerInfo {
        private UUID playerID;
        private String playerName;
        private boolean ready;

        public LobbyPlayerInfo() {
        }

        public LobbyPlayerInfo(UUID playerID, String playerName, boolean ready) {
            this.playerID = playerID;
            this.playerName = playerName;
            this.ready = ready;
        }

        public UUID getPlayerID() { return playerID; }
        public String getPlayerName() { return playerName; }
        public boolean isReady() { return ready; }
    }
}
