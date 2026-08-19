package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class SelectMapRequest extends Request {

    private int mapIndex;

    public SelectMapRequest() {
        super();
    }

    public SelectMapRequest(UUID senderID, int mapIndex) {
        super(MessageType.SELECT_MAP_REQUEST, senderID, System.currentTimeMillis());
        this.mapIndex = mapIndex;
    }

    public int getMapIndex() {
        return mapIndex;
    }
}
