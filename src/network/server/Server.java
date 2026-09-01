package network.server;

import model.state.Lobby;
import network.request.EndTurnRequest;
import network.request.JoinLobbyRequest;
import network.request.ReadyToggleRequest;
import network.request.SelectMapRequest;
import network.request.StartGameRequest;
import network.server.handlers.EndTurnHandler;
import network.server.handlers.JoinLobbyHandler;
import network.server.handlers.ReadyToggleHandler;
import network.server.handlers.SelectMapHandler;
import network.server.handlers.StartGameHandler;
import network.server.tcp.TCPServer;
import network.server.udp.UDPServer;

public class Server {

    private TCPServer tcpServer;
    private UDPServer udpServer;
    private Thread TCPThread;
    private Thread UDPThread;
    private GameServer gameServer;
    private Lobby lobby;

    public Server() {
        this.tcpServer = new TCPServer();
        this.udpServer = new UDPServer();
        this.gameServer = new GameServer();
        this.lobby = new Lobby(null);

        tcpServer.setGameServer(gameServer);
        udpServer.setGameServer(gameServer);
        gameServer.setLobby(lobby);
        gameServer.setMessenger(tcpServer);

        this.TCPThread = new Thread(tcpServer);
        this.UDPThread = new Thread(udpServer);

        registerHandlers();
    }

    private void registerHandlers() {
        gameServer.register(JoinLobbyRequest.class, new JoinLobbyHandler(gameServer));
        gameServer.register(ReadyToggleRequest.class, new ReadyToggleHandler(gameServer));
        gameServer.register(SelectMapRequest.class, new SelectMapHandler(gameServer));
        gameServer.register(StartGameRequest.class, new StartGameHandler(gameServer));
        gameServer.register(EndTurnRequest.class, new EndTurnHandler(gameServer));
    }

    public void start() {
        TCPThread.start();
        System.out.println("TCP Server started ...");
        UDPThread.start();
        System.out.println("UDP Server started ...");
    }

    public void stop() {
        tcpServer.stop();
        System.out.println("TCP Server stopped ...");
        udpServer.stop();
        System.out.println("UDP Server stopped ...");
    }

    public TCPServer getTcpServer() { return tcpServer; }
    public UDPServer getUdpServer() { return udpServer; }
    public GameServer getGameServer() { return gameServer; }
    public Lobby getLobby() { return lobby; }
}