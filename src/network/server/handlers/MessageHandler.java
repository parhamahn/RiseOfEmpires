package network.server.handlers;

import network.common.Message;

import java.util.UUID;

public interface MessageHandler<T extends Message> {

    void handle(UUID senderID, T message);

}
