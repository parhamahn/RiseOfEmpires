package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class DeclareWarRequest extends Request {

    private UUID targetPlayerID;

    public DeclareWarRequest() {
        super();
    }

    public DeclareWarRequest(UUID senderID, UUID targetPlayerID) {
        super(MessageType.DECLARE_WAR_REQUEST, senderID, System.currentTimeMillis());
        this.targetPlayerID = targetPlayerID;
    }

    public UUID getTargetPlayerID() {
        return targetPlayerID;
    }
}
