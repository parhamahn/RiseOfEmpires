package network.server.udp;

import com.google.gson.JsonObject;
import network.common.JsonSerializer;
import network.common.Message;
import network.server.GameServer;
import network.server.Messenger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UDPServer implements Runnable, Messenger {

    private static final int BUFFER_SIZE = 8192;
    public final int PORT = 1920;

    private GameServer gameServer;
    private DatagramSocket socket;
    private volatile boolean running;
    private final Map<UUID, SocketAddress> clientAddresses = new ConcurrentHashMap<>();

    public UDPServer() {
        try {
            this.socket = new DatagramSocket(PORT);
        } catch (IOException e) {
            System.out.println("Error while starting the UDP server : " + e);
        }
    }

    public void start() {
        running = true;
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        running = true;
        byte[] buffer = new byte[BUFFER_SIZE];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String jsonText = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                JsonObject json = JsonSerializer.GSON.fromJson(jsonText, JsonObject.class);
                Message message = JsonSerializer.fromJson(json);

                if (message.getSenderID() != null) {
                    clientAddresses.put(message.getSenderID(), packet.getSocketAddress());
                }
                processMessage(message.getSenderID(), message);
            } catch (IOException e) {
                if (running) System.out.println("UDP receive error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Malformed UDP packet ignored: " + e.getMessage());
            }
        }
    }

    public void processMessage(UUID clientID, Message message) {
        gameServer.processMessage(clientID, message);
    }

    public void sendTo(UUID clientID, Message message) {
        SocketAddress address = clientAddresses.get(clientID);
        if (address == null) return;
        sendTo(address, message);
    }

    private void sendTo(SocketAddress address, Message message) {
        try {
            JsonObject json = JsonSerializer.toJson(message);
            byte[] data = JsonSerializer.GSON.toJson(json).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(data, data.length, address));
        } catch (IOException e) {
            System.err.println("Failed to send UDP message: " + e.getMessage());
        }
    }

    public void broadcast(Message message) {
        for (SocketAddress address : clientAddresses.values()) {
            sendTo(address, message);
        }
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void removeClient(UUID clientID) {
        clientAddresses.remove(clientID);
    }

    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) socket.close();
    }
}