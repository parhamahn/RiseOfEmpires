package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class ReadyToggleRequest extends Request {

    private boolean ready;

    public ReadyToggleRequest() {
        super();
    }

    public ReadyToggleRequest(UUID senderID, boolean ready) {
        super(MessageType.READY_TOGGLE_REQUEST, senderID, System.currentTimeMillis());
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
