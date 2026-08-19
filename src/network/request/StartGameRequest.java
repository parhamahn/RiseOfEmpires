package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class StartGameRequest extends Request {

    public StartGameRequest() {
        super();
    }

    public StartGameRequest(UUID senderID) {
        super(MessageType.START_GAME_REQUEST, senderID, System.currentTimeMillis());
    }
}
