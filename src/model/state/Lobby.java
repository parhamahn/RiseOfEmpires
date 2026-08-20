package model.state;

import model.Entity;
import model.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby extends Entity {
    private final Map<UUID, Player> players = new ConcurrentHashMap<>();
    private UUID hostId;
    private int selectedMapIndex;
    private boolean started;
    private GameState gameState;

    public Lobby(UUID hostId){
        super();
        this.hostId = hostId;
        this.started = false;
    }


    public Map<UUID, Player> getPlayers() {
        return players;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public int getSelectedMapIndex() {
        return selectedMapIndex;
    }

    public void setSelectedMapIndex(int selectedMapIndex) {
        this.selectedMapIndex = selectedMapIndex;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}