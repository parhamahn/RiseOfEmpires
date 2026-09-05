package network.client;

import network.client.tcp.TCPClient;
import network.client.udp.UDPClient;
import network.common.Message;
import network.common.Request;
import network.request.UdpPingRequest;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class NetworkManager implements MessageListener {

    private final TCPClient tcpClient;
    private final UDPClient udpClient;
    private final List<Consumer<Message>> listeners = new ArrayList<>();
    private UUID myPlayerId;

    public NetworkManager() {
        this.tcpClient = new TCPClient();
        this.tcpClient.setListener(this);
        this.udpClient = new UDPClient();
        this.udpClient.setListener(this);
    }

    public void connect(String host, int port, UUID myPlayerId) throws IOException {
        this.myPlayerId = myPlayerId;
        tcpClient.connect(host, port);
        udpClient.connect(host, port + 1);
        udpClient.send(new UdpPingRequest(myPlayerId));
    }

    public void send(Request request) {
        tcpClient.send(request);
    }

    public void sendUdp(Message message) {
        udpClient.send(message);
    }

    public void addListener(Consumer<Message> listener) {
        listeners.add(listener);
    }

    public UUID getMyPlayerId() { return myPlayerId; }
    public boolean isConnected() { return tcpClient.isConnected(); }
    public void disconnect() { tcpClient.disconnect(); udpClient.disconnect(); }

    @Override
    public void onMessageReceived(Message message) {
        SwingUtilities.invokeLater(() -> {
            for (Consumer<Message> listener : new ArrayList<>(listeners)) {
                listener.accept(message);
            }
        });
    }
}