package network.client.tcp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import network.client.MessageListener;
import network.common.JsonSerializer;
import network.common.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient implements Runnable {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private volatile boolean connected;
    private MessageListener listener;
    private final Gson gson = new Gson();

    public TCPClient() {
        this.connected = false;
    }

    public void connect(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.connected = true;

        Thread listenerThread = new Thread(this);
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    public void send(Message message) {
        if (connected && writer != null) {
            JsonObject json = JsonSerializer.toJson(message);
            writer.println(gson.toJson(json));
        }
    }

    @Override
    public void run() {
        String line;
        try {
            while (connected && (line = reader.readLine()) != null) {
                JsonObject json = gson.fromJson(line, JsonObject.class);
                Message message = JsonSerializer.fromJson(json);
                if (listener != null) {
                    listener.onMessageReceived(message);
                }
            }
        } catch (IOException e) {
            if (connected) {
                System.err.println("Connection lost: " + e.getMessage());
            }
        } finally {
            disconnect();
        }
    }

    public void disconnect() {
        connected = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
