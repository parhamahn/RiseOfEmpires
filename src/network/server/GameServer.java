package network.server;

import network.common.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class GameServer {

    private final ReentrantLock gameLock = new ReentrantLock();
    private final Map<Class<? extends Message>, MessageHandler<? extends Message>> handlers = new HashMap<>();

    public <T extends Message> void register(Class<T> type, MessageHandler<T> handler) {
        handlers.put(type, handler);
    }

    public void processMessage(UUID playerId, Message message) {
        gameLock.lock();
        try {
            MessageHandler<Message> handler = (MessageHandler<Message>) handlers.get(message.getClass());
            if (handler != null) handler.handle(playerId, message);
            else System.err.println("No handler for " + message.getClass().getSimpleName());
        } finally {
            gameLock.unlock();
        }
    }
}
