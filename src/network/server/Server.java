package network.server;

import network.server.tcp.TCPServer;
import network.server.udp.UDPServer;

public class Server {

    private TCPServer tcpServer;
    private UDPServer udpServer;
    private Thread TCPThread;
    private Thread UDPThread;
    private GameServer gameServer;

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
        TCPThread.interrupt();
        UDPThread.interrupt();
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
}
