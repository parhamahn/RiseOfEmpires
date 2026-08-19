package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class EndTurnRequest extends Request {

    public EndTurnRequest() {
        super();
    }

    public EndTurnRequest(UUID senderID) {
        super(MessageType.END_TURN_REQUEST, senderID, System.currentTimeMillis());
    }
}
