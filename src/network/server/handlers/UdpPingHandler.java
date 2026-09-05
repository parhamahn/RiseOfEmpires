package network.server.handlers;

import network.request.UdpPingRequest;
import network.server.MessageHandler;

import java.util.UUID;

public class UdpPingHandler implements MessageHandler<UdpPingRequest> {

    @Override
    public void handle(UUID senderID, UdpPingRequest message) {
    }
}