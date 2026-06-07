package controller;

import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class GameController {
    private GamePanel gamePanel;
    private JPanel cards;

    public GameController(GamePanel gamePanel, JPanel cards){
        this.gamePanel = gamePanel;
        this.cards = cards;
        this.attachListeners();
    }

    private void attachListeners() {
        gamePanel.getHudPanel().getMenuButton().addActionListener(e -> menuAction());
    }

    private void menuAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }
}