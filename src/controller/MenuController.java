package controller;
import model.state.GameState;
import view.game.GamePanel;
import view.menu.MenuPanel;
import javax.swing.*;
import java.awt.*;

public class MenuController {
    private MenuPanel menuPanel;
    private JPanel cards;
    private GameState gameState;
    private GamePanel gamePanel;
    private SelectionContext selectionContext;

    public MenuController(MenuPanel menuPanel, JPanel cards, GameState gameState, GamePanel gamePanel, SelectionContext selectionContext){
        this.menuPanel = menuPanel;
        this.cards = cards;
        this.attachListeners();
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.selectionContext = selectionContext;
    }

    public void attachListeners(){
        menuPanel.getStartButton().addActionListener(e -> startAction());
        menuPanel.getSettingsButton().addActionListener(e -> settingsAction());
        menuPanel.getExitButton().addActionListener(e -> exitAction());
    }

    public void startAction(){
        gameState.reset();
        gamePanel.reset();
        selectionContext.reset();
        gamePanel.getMapPanel().setMap(gameState.getWorldState().getMap());
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "game");
    }

    public void settingsAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "settings");
    }

    public void exitAction(){
        int sure = JOptionPane.showConfirmDialog(menuPanel, "Are you sure ?", "Exit", JOptionPane.YES_OPTION);
        if(sure==JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }

}
