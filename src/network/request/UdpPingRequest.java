package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class UdpPingRequest extends Request {

    public UdpPingRequest() {
        super();
    }

    public UdpPingRequest(UUID senderID) {
        super(MessageType.UDP_PING, senderID, System.currentTimeMillis());
    }
}