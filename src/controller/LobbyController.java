package controller;

import model.state.Lobby;
import network.server.Server;
import view.multiplayer.LobbyPanel;

import javax.swing.*;
import java.awt.*;

public class LobbyController {
    private LobbyPanel lobbyPanel;
    private JPanel cards;
    private Server server;

    public LobbyController(LobbyPanel lobbyPanel, JPanel cards) {
        this.lobbyPanel = lobbyPanel;
        this.cards = cards;

        this.attachListeners();
    }

    public void attachListeners(){
        lobbyPanel.getExitButton().addActionListener(e->exitAction());
    }

    public void setServer(Server server){
        this.server = server;
    }

    public void exitAction(){
        if(server != null){
            server.stop();
            server = null;
        }
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }
}
