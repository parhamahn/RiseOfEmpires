package controller.handler;

import controller.SelectionContext;
import model.state.GameState;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

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
        gamePanel.getHudPanel().getEndTurnButton().addActionListener(e -> turnHandler.endTurnAction());


        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "menu");
        actionMap.put("menu",  new AbstractAction() {
            public void actionPerformed(ActionEvent e) { menuAction(); }
        });

    }


    private void menuAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }
}
