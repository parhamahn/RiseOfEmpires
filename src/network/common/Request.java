package network.common;

import java.util.UUID;

public abstract class Request extends Message {

    protected Request() {
        super();
    }

    protected Request(MessageType messageType, UUID senderID, long timeStamp) {
        super(messageType, senderID, timeStamp);
    }
}
