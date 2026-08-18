package network.common;

import java.util.UUID;

public abstract class Response extends Message{
    protected Response(MessageType messageType, UUID senderID, long timeStamp) {
        super(messageType, senderID, timeStamp);
    }
}
