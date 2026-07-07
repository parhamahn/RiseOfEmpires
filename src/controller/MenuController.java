package controller;
import view.menu.MenuPanel;
import javax.swing.*;
import java.awt.*;

public class MenuController {
    private MenuPanel menuPanel;
    private JPanel cards;

    public MenuController(MenuPanel menuPanel, JPanel cards){
        this.menuPanel = menuPanel;
        this.cards = cards;
        this.attachListeners();
    }

    public void attachListeners(){
        menuPanel.getStartButton().addActionListener(e -> startAction());
        menuPanel.getSettingsButton().addActionListener(e -> settingsAction());
        menuPanel.getExitButton().addActionListener(e -> exitAction());
    }

    public void startAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        menuPanel.getStartButton().setText("Continue");
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
