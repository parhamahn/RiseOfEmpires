package controller.handler;

import view.game.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class CameraHandler {
    private final GamePanel gamePanel;

    public CameraHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void attachCameraListeners() {
        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "zoomIn");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "zoomOut");

        actionMap.put("zoomIn",  new AbstractAction() {public void actionPerformed(ActionEvent e) { if(gamePanel.isAnyPanelOpen())return; gamePanel.getMapPanel().zoom(1); }});
        actionMap.put("zoomOut", new AbstractAction() {public void actionPerformed(ActionEvent e) { if(gamePanel.isAnyPanelOpen())return; gamePanel.getMapPanel().zoom(-1); }});
        actionMap.put("moveUp", new AbstractAction() {public void actionPerformed(ActionEvent e) { if(gamePanel.isAnyPanelOpen())return; gamePanel.getMapPanel().moveCamera(0, -100); }});
        actionMap.put("moveDown", new AbstractAction() {public void actionPerformed(ActionEvent e) { if(gamePanel.isAnyPanelOpen())return; gamePanel.getMapPanel().moveCamera(0, 100); }});
        actionMap.put("moveLeft", new AbstractAction() {public void actionPerformed(ActionEvent e) { if(gamePanel.isAnyPanelOpen())return; gamePanel.getMapPanel().moveCamera(-100, 0); }});
        actionMap.put("moveRight", new AbstractAction() {public void actionPerformed(ActionEvent e) { if(gamePanel.isAnyPanelOpen())return; gamePanel.getMapPanel().moveCamera(100, 0); }});
    }
}
