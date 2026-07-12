package controller.handler;

import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;

public class HUDHandler {

    private JPanel cards;
    private GamePanel gamePanel;
    private TurnHandler turnHandler;


    public HUDHandler(GamePanel gamePanel, JPanel cards, TurnHandler turnHandler) {
        this.gamePanel = gamePanel;
        this.cards = cards;
        this.turnHandler = turnHandler;
    }

    public void attachHUDHandler(){
        gamePanel.getHudPanel().getEndTurnButton().addActionListener(e -> { if(gamePanel.isAnyPanelOpen())return; turnHandler.endTurnAction();});

    }


    private void menuAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }
}
