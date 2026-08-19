package network.client.udp;

import com.google.gson.JsonObject;
import network.client.MessageListener;
import network.common.JsonSerializer;
import network.common.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPClient implements Runnable {

    private static final int BUFFER_SIZE = 8192;

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private volatile boolean connected;
    private MessageListener listener;

    public void connect(String host, int port) throws IOException {
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
        this.socket = new DatagramSocket();
        this.connected = true;

        Thread listenerThread = new Thread(this);
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    public void send(Message message) {
        if (!connected || socket == null) return;
        try {
            JsonObject json = JsonSerializer.toJson(message);
            byte[] data = JsonSerializer.GSON.toJson(json).getBytes(StandardCharsets.UTF_8);
            if (data.length > BUFFER_SIZE) {
                System.err.println("UDP message too large (" + data.length + " bytes), dropping");
                return;
            }
            socket.send(new DatagramPacket(data, data.length, serverAddress, serverPort));
        } catch (IOException e) {
            System.err.println("Failed to send UDP message: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        while (connected) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String jsonText = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                JsonObject json = JsonSerializer.GSON.fromJson(jsonText, JsonObject.class);
                Message message = JsonSerializer.fromJson(json);
                if (listener != null) {
                    listener.onMessageReceived(message);
                }
            } catch (IOException e) {
                if (connected) System.err.println("UDP receive error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Malformed UDP packet ignored: " + e.getMessage());
            }
        }
    }

    public void disconnect() {
        connected = false;
        if (socket != null && !socket.isClosed()) socket.close();
    }

    public boolean isConnected() {
        return connected;
    }
}