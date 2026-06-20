package controller;

import helpers.SoundEffects;
import model.GameState;
import model.Hex;
import view.GamePanel;
import view.MapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameController {
    private GamePanel gamePanel;
    private JPanel cards;
    private GameState gameState;

    public GameController(GamePanel gamePanel, JPanel cards, GameState gameState){
        this.gamePanel = gamePanel;
        this.cards = cards;
        this.gameState = gameState;
        this.attachHUDListeners();
        this.attachCameraListeners();
        this.attachHexSelectionListener();
    }

    private void attachHUDListeners() {
        gamePanel.getHudPanel().getMenuButton().addActionListener(e -> menuAction());
    }

    private void attachCameraListeners(){
        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");

        actionMap.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.getMapPanel().moveCamera(0, -50);
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.getMapPanel().moveCamera(0, 50);
            }
        });
        actionMap.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.getMapPanel().moveCamera(-50, 0);
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.getMapPanel().moveCamera(50, 0);
            }
        });
    }

    private void attachHexSelectionListener() {
        MapPanel mapPanel = gamePanel.getMapPanel();
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Hex clicked = mapPanel.getHexAt(e.getX(), e.getY());
                if (clicked != null) {
                    for(Hex neighbour:gameState.getMap().getNeighbourHexes(clicked)) {
                        if(neighbour.isDiscovered()) {
                            gameState.setSelectedHex(clicked);
                            SoundEffects.getInstance().playSelectHexSoundEffect();
                            return;
                        }
                    }
                }
            }
        });
    }

    private void menuAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }
}