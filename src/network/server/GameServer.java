package network.server;

import model.state.GameState;
import model.state.Lobby;
import network.common.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class GameServer {

    private final ReentrantLock gameLock = new ReentrantLock();
    private final Map<Class<? extends Message>, MessageHandler<? extends Message>> handlers = new HashMap<>();
    private volatile GameState game;
    private Lobby lobby;
    private Messenger messenger;

    public void setGameState(GameState game) { this.game = game; }
    public GameState getGameState() { return game; }
    public void setLobby(Lobby lobby) { this.lobby = lobby; }
    public Lobby getLobby() { return lobby; }
    public void setMessenger(Messenger messenger) { this.messenger = messenger; }
    public Messenger getMessenger() { return messenger; }

    public <T extends Message> void register(Class<T> type, MessageHandler<T> handler) {
        handlers.put(type, handler);
    }

    public void processMessage(UUID playerId, Message message) {
        gameLock.lock();
        GameState snapshot = game;
        UUID previousContext = null;
        boolean contextSet = false;
        try {
            if (snapshot != null) {
                previousContext = snapshot.getActiveContextId();
                snapshot.setActiveContextId(playerId);
                contextSet = true;
            }
            MessageHandler<Message> handler = (MessageHandler<Message>) handlers.get(message.getClass());
            if (handler != null) handler.handle(playerId, message);
            else System.err.println("No handler for " + message.getClass().getSimpleName());
        } finally {
            if (snapshot != null && contextSet) snapshot.setActiveContextId(previousContext);
            gameLock.unlock();
        }
    }
}