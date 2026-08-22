package network.server.tcp;

import network.common.JsonSerializer;
import network.common.Message;
import network.server.GameServer;
import network.server.Messenger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TCPServer implements Messenger, Runnable {

    public final int PORT = 1919;

    private GameServer gameServer;
    private ServerSocket serverSocket;
    private Map<UUID, TCPClientHandler> clients;
    private List<Thread> threadPool;

    public TCPServer() {
        clients = new ConcurrentHashMap<>();
        threadPool = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("Error while starting the TCP server : "+ e);
        }
    }


    private void start() throws IOException {
        while(true){
            Socket NewSocket = serverSocket.accept();
            acceptClient(NewSocket);
        }
    }


    public void acceptClient(Socket socket){
        UUID clientID = UUID.randomUUID();
        TCPClientHandler clientHandler = new TCPClientHandler(clientID, this, socket);
        Thread thread = new Thread(clientHandler);
        thread.start();
        threadPool.add(thread);
        clients.put(clientID, clientHandler);
    }

    public void processMessage(UUID clientID, Message message){
        gameServer.processMessage(clientID, message);
    }

    public void broadcast(Message message){
        for (TCPClientHandler clientHandler:clients.values()){
            clientHandler.sendMessage(message);
        }
    }

    public void sendTo(UUID playerId, Message message) {
        TCPClientHandler handler = clients.get(playerId);
        if (handler != null) handler.sendMessage(message);
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void removeClient(UUID clientID){
        clients.remove(clientID);
    }

    public void stop(){
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error stopping TCP server : " + e);
        }
        for (TCPClientHandler clientHandler : clients.values()) {
            clientHandler.disconnect();
        }
        clients.clear();
    }

    @Override
    public void run() {
        try {
            start();
        } catch (IOException e) {
            if (serverSocket == null || !serverSocket.isClosed()) {
                System.out.println("Error starting TCP server : "+e);
            }
        }
    }
}
