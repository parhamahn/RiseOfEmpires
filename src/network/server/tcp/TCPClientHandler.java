package network.server.tcp;

import com.google.gson.JsonObject;
import network.common.JsonSerializer;
import network.common.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class TCPClientHandler implements Runnable{

    private final UUID clientID;
    private final Socket socket;
    private final TCPServer server;
    private PrintWriter writer;
    private BufferedReader reader;
    private volatile boolean connected;


    public TCPClientHandler(UUID clientID, TCPServer server, Socket socket) {
        this.clientID = clientID;
        this.socket = socket;
        this.connected = true;
        this.server = server;

        try {
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error setting up streams for client " + clientID + ": \n"+e);
            this.connected = false;
        }

    }

    public UUID getClientID() {
        return clientID;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }


    @Override
    public void run() {
        String messageString;
        try {
            while (connected && (messageString = reader.readLine()) != null) {
                System.out.println("Received from client " + clientID);
                JsonObject json = JsonSerializer.GSON.fromJson(messageString, JsonObject.class);
                Message message = JsonSerializer.fromJson(json);
                server.processMessage(clientID, message);
            }
        } catch (IOException e) {
            if (connected) {
                System.out.println("Client " + clientID + " connection error: " + e.getMessage());
            }
        } finally {
            disconnect();
        }
    }

    public void sendMessage(Message message) {
        if (connected && writer != null) {
            JsonObject json = JsonSerializer.toJson(message);
            writer.println(JsonSerializer.GSON.toJson(json));
        }
    }


    public void disconnect() {
        connected = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        server.removeClient(clientID);
    }

}
