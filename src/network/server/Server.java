package network.server;

import model.state.Lobby;
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
        tcpServer.setGameServer(gameServer);
        udpServer.setGameServer(gameServer);
        this.TCPThread = new Thread(tcpServer);
        this.UDPThread = new Thread(udpServer);
    }

    public void start(){
        TCPThread.start();
        System.out.println("TCP Server started ...");
        UDPThread.start();
        System.out.println("UDP Server started ...");
    }

    public void stop(){
        tcpServer.stop();
        System.out.println("TCP Server stopped ...");
        udpServer.stop();
        System.out.println("UDP Server stopped ...");
    }

    public TCPServer getTcpServer() {
        return tcpServer;
    }

    public UDPServer getUdpServer() {
        return udpServer;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
