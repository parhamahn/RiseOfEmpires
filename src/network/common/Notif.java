package network.common;

import java.util.UUID;

public abstract class Notif extends Message {

    protected Notif() {
        super();
    }

    protected Notif(MessageType messageType, UUID senderID, long timeStamp) {
        super(messageType, senderID, timeStamp);
    }
}
