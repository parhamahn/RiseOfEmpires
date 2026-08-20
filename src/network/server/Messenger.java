package network.server;

import network.common.Message;

import java.util.UUID;

public interface Messenger {
    void sendTo(UUID playerID, Message message);
    void broadcast(Message message);
}
