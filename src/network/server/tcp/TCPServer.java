package network.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TCPServer {

    private ServerSocket serverSocket;
    private Map<UUID, TCPClientHandler> clients;
    private List<Thread> threadPool;
    private final int PORT = 1919;


    public TCPServer() {
        clients = new ConcurrentHashMap<>();
        threadPool = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("Error while starting the TCP server : "+ e);
        }
    }


    public void start() throws IOException {
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

    public void processMessage(UUID clientID, String message){
        System.out.println("Server --> "+clientID+" : "+message);
    }

    public void broadcast(String message){
        for (TCPClientHandler clientHandler:clients.values()){
            clientHandler.sendMessage(message);
        }
    }

    public void removeClient(UUID clientID){
        clients.remove(clientID);
    }
}
