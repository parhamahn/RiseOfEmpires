package network.client;

import network.common.Message;

public interface MessageListener {
    void onMessageReceived(Message message);
}
