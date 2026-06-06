package controller;

import view.MenuPanel;

import javax.swing.*;

public class MenuController {
    private MenuPanel menuPanel;

    public MenuController(MenuPanel menuPanel){
        this.menuPanel = menuPanel;
        this.attachListeners();
    }

    public void attachListeners(){
        menuPanel.getStartButton().addActionListener(e -> startAction());
        menuPanel.getSettingsButton().addActionListener(e -> settingsAction());
        menuPanel.getExitButton().addActionListener(e -> exitAction());
    }

    public void startAction(){
        JOptionPane.showMessageDialog(menuPanel, "Start clicked");
    }

    public void settingsAction(){
        JOptionPane.showMessageDialog(menuPanel, "Settings clicked");
    }

    public void exitAction(){
        int sure = JOptionPane.showConfirmDialog(menuPanel, "Are you sure ?", "Exit", JOptionPane.YES_OPTION);
        if(sure==JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }

}
