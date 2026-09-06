package controller.handler;

import model.state.GameState;
import network.client.NetworkManager;
import network.request.ChatMessageRequest;
import view.game.ChatSection;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.UUID;

public class ChatHandler {

    private GameState gameState;
    private GamePanel gamePanel;
    private NetworkManager networkManager;

    public ChatHandler(GameState gameState, GamePanel gamePanel) {

        this.gamePanel = gamePanel;
        this.gameState = gameState;

    }

    public void attachHandlers() {

        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_QUOTE, 0), "chatToggle");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "sendMessage");

        actionMap.put("chatToggle",  new AbstractAction() {public void actionPerformed(ActionEvent e){chatToggle();}});
        actionMap.put("sendMessage",  new AbstractAction() {public void actionPerformed(ActionEvent e){sendMessage();}});

    }

    public void chatToggle(){
        ChatSection chatSection = gamePanel.getChatSection();
        if(chatSection.isVisible()){
            gamePanel.setAnyPanelOpen(false);
            chatSection.hideChat();
        }else{
            gamePanel.setAnyPanelOpen(true);
            chatSection.showChat();
        }
    }

    public void sendMessage(){
        ChatSection chatSection = gamePanel.getChatSection();
        UUID senderID = gameState.getLocalPlayerId();
        String string = chatSection.getChatInput().getText();
        if(string!=null && !string.isBlank() && chatSection.isVisible()){
            if (networkManager != null) {
                networkManager.sendUdp(new ChatMessageRequest(senderID, string));
            } else {
                chatSection.addChat("You", string,
                        new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date()));
            }
            chatSection.getChatInput().setText("");
        }
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

}
