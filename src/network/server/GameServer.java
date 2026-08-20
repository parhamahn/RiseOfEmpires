package network.server;

import network.common.Message;
import network.server.handlers.MessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameServer {

    private final Map<Class<? extends Message>, MessageHandler<? extends Message>> handlers = new HashMap<>();

    public <T extends Message> void register(Class<T> type, MessageHandler<T> handler) {
        handlers.put(type, handler);
    }

    public void processMessage(UUID playerId, Message message) {
        MessageHandler<Message> handler = (MessageHandler<Message>) handlers.get(message.getClass());
        if (handler != null) handler.handle(playerId, message);
        else System.err.println("No handler for " + message.getClass().getSimpleName());
    }
}
