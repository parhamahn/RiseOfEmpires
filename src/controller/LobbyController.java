package controller;

import model.state.GameState;
import network.client.NetworkManager;
import network.common.Message;
import network.dto.mapper.GameStateMapper;
import network.notif.ChatMessageNotif;
import network.notif.GameStateNotif;
import network.notif.LobbyStateNotif;
import network.request.EndTurnRequest;
import network.request.JoinLobbyRequest;
import network.request.ReadyToggleRequest;
import network.request.SelectMapRequest;
import network.request.StartGameRequest;
import network.response.ActionResultResponse;
import network.server.Server;
import model.map.MapCatalog;
import view.game.GamePanel;
import view.multiplayer.LobbyPanel;
import view.multiplayer.MultiplayerOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyController {

    private static final int PORT = 1919;

    private final LobbyPanel lobbyPanel;
    private final JPanel cards;
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final SelectionContext selectionContext;

    private GameController gameController;
    private Server hostedServer;
    private NetworkManager networkManager;
    private UUID myPlayerId;
    private String myPlayerName;
    private UUID lastKnownHostId;
    private boolean ready;

    public LobbyController(LobbyPanel lobbyPanel, JPanel cards, GameState gameState,
                           GamePanel gamePanel, SelectionContext selectionContext) {
        this.lobbyPanel = lobbyPanel;
        this.cards = cards;
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.selectionContext = selectionContext;

        for (String name : MapCatalog.MAP_NAMES) {
            lobbyPanel.getMapSelector().addItem(name);
        }

        attachListeners();
    }

    private void attachListeners() {
        lobbyPanel.getExitButton().addActionListener(e -> exitAction());

        lobbyPanel.getReadyButton().addActionListener(e -> {
            ready = !ready;
            lobbyPanel.getReadyButton().setText(ready ? "Not Ready" : "Ready");
            if (networkManager != null) {
                networkManager.send(new ReadyToggleRequest(myPlayerId, ready));
            }
        });

        lobbyPanel.getStartButton().addActionListener(e -> {
            if (networkManager != null) {
                networkManager.send(new StartGameRequest(myPlayerId));
            }
        });

        lobbyPanel.getMapSelector().addActionListener(e -> {
            if (networkManager == null || lastKnownHostId == null || !lastKnownHostId.equals(myPlayerId)) return;
            int index = lobbyPanel.getMapSelector().getSelectedIndex();
            if (index >= 0) networkManager.send(new SelectMapRequest(myPlayerId, index));
        });
    }

    public void startAsHost() {
        String name = promptName();
        if (name == null) return;

        hostedServer = new Server();
        hostedServer.start();
        connect("localhost", name);
    }

    public void startAsJoiner() {
        String address = MultiplayerOption.joinAddress();
        if (address == null || address.isBlank()) return;
        String name = promptName();
        if (name == null) return;
        connect(address.trim(), name);
    }

    private String promptName() {
        String name = JOptionPane.showInputDialog(null, "Enter your username:", "Multiplayer", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.isBlank()) return null;
        return name.trim();
    }

    private void connect(String host, String name) {
        myPlayerId = UUID.randomUUID();
        myPlayerName = name;
        ready = false;
        lastKnownHostId = null;
        lobbyPanel.getReadyButton().setText("Ready");
        lobbyPanel.setStatus("Connecting...");
        lobbyPanel.setPlayerRows(new ArrayList<>());

        networkManager = new NetworkManager();
        networkManager.addListener(this::onMessage);

        try {
            networkManager.connect(host, PORT, myPlayerId);
            if (gameController != null) gameController.setNetworkManager(networkManager);
            networkManager.send(new JoinLobbyRequest(myPlayerId, myPlayerName));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to " + host + ": " + e.getMessage(),
                    "Connection Failed", JOptionPane.ERROR_MESSAGE);
            networkManager = null;
            if (hostedServer != null) {
                hostedServer.stop();
                hostedServer = null;
            }
            return;
        }

        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "lobby");
    }

    private void onMessage(Message message) {
        if (message instanceof LobbyStateNotif notif) {
            handleLobbyState(notif);
        } else if (message instanceof GameStateNotif notif) {
            handleGameState(notif);
        } else if (message instanceof ActionResultResponse response && !response.isSuccess()) {
            JOptionPane.showMessageDialog(lobbyPanel, response.getErrorMessage(), "Action Rejected", JOptionPane.WARNING_MESSAGE);
        } else if (message instanceof ChatMessageNotif notif) {
            gamePanel.getChatSection().addChat(notif.getSenderName(), notif.getContent());
        }
    }

    private void handleLobbyState(LobbyStateNotif notif) {
        lastKnownHostId = notif.getHostID();
        boolean isHost = myPlayerId.equals(lastKnownHostId);

        List<LobbyPanel.PlayerRow> rows = new ArrayList<>();
        for (LobbyStateNotif.LobbyPlayerInfo info : notif.getPlayers()) {
            String tag = info.getPlayerID().equals(lastKnownHostId) ? " (Host)" : "";
            String status = info.isReady() ? " - Ready" : " - Not Ready";
            rows.add(new LobbyPanel.PlayerRow(info.getPlayerName() + tag + status, info.isReady()));
        }
        lobbyPanel.setPlayerRows(rows);
        lobbyPanel.setStatus(isHost ? "You are the host" : "Waiting for the host to start the game...");

        lobbyPanel.getMapSelector().setEnabled(isHost);
        if (notif.getSelectedMapIndex() >= 0
                && lobbyPanel.getMapSelector().getSelectedIndex() != notif.getSelectedMapIndex()) {
            lobbyPanel.getMapSelector().setSelectedIndex(notif.getSelectedMapIndex());
        }

        boolean allReady = !notif.getPlayers().isEmpty()
                && notif.getPlayers().stream().allMatch(LobbyStateNotif.LobbyPlayerInfo::isReady);
        lobbyPanel.getStartButton().setEnabled(isHost && allReady);
    }

    private void handleGameState(GameStateNotif notif) {
        try {
            GameState received = GameStateMapper.INSTANCE.fromDTO(notif.getState(), myPlayerId);

            gameState.loadFrom(received);
            gameState.setLocalPlayerId(myPlayerId);

            gamePanel.reset();
            gamePanel.getMapPanel().setMap(gameState.getWorldState().getMap());
            gamePanel.getHudPanel().refresh();
            selectionContext.reset();
            gamePanel.setAnyPanelOpen(false);

            rewireEndTurnButton();

            CardLayout cl = (CardLayout) (cards.getLayout());
            cl.show(cards, "game");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(lobbyPanel, "Failed to load game state: " + e.getMessage(),
                    "Sync Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rewireEndTurnButton() {
        JButton endTurnButton = gamePanel.getHudPanel().getEndTurnButton();
        for (ActionListener al : endTurnButton.getActionListeners()) {
            endTurnButton.removeActionListener(al);
        }
        endTurnButton.addActionListener(e -> {
            if (gamePanel.isAnyPanelOpen()) return;
            networkManager.send(new EndTurnRequest(myPlayerId));
        });
    }

    public void exitAction() {
        if (networkManager != null) {
            networkManager.disconnect();
            networkManager = null;
        }
        if (hostedServer != null) {
            hostedServer.stop();
            hostedServer = null;
        }
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }

    public NetworkManager getNetworkManager() { return networkManager; }
    public UUID getMyPlayerId() { return myPlayerId; }
    public void setGameController(GameController gameController) { this.gameController = gameController; }
}