package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class AllianceRequest extends Request {

    private UUID targetPlayerID;

    public AllianceRequest() {
        super();
    }

    public AllianceRequest(UUID senderID, UUID targetPlayerID) {
        super(MessageType.ALLIANCE_REQUEST, senderID, System.currentTimeMillis());
        this.targetPlayerID = targetPlayerID;
    }

    public UUID getTargetPlayerID() {
        return targetPlayerID;
    }
}
