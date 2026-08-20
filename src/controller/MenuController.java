package controller;
import helpers.sound.SoundEffects;
import model.state.GameState;
import network.server.GameServer;
import network.server.Server;
import view.game.GamePanel;
import view.menu.MenuPanel;
import view.multiplayer.MultiplayerOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MenuController {
    private MenuPanel menuPanel;
    private JPanel cards;
    private GameState gameState;
    private GamePanel gamePanel;
    private SelectionContext selectionContext;

    public MenuController(MenuPanel menuPanel, JPanel cards, GameState gameState, GamePanel gamePanel, SelectionContext selectionContext){
        this.menuPanel = menuPanel;
        this.cards = cards;
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.selectionContext = selectionContext;
        this.attachListeners();
    }

    public void attachListeners(){

        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();

        menuPanel.getStartButton().addActionListener(e -> startAction());
        menuPanel.getSettingsButton().addActionListener(e -> settingsAction());
        menuPanel.getExitButton().addActionListener(e -> exitAction());
        menuPanel.getMultiplayerButton().addActionListener(e -> multiplayerAction());
        gamePanel.getPausePanel().getMainMenuButton().addActionListener(e -> menuAction());
        gamePanel.getPausePanel().getContinueButton().addActionListener(e -> continueAction());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "menu");
        actionMap.put("menu",  new AbstractAction() {public void actionPerformed(ActionEvent e) {attachPauseAction();}});

    }

    public void startAction(){
        gameState.reset();
        gamePanel.reset();
        selectionContext.reset();
        gamePanel.getMapPanel().setMap(gameState.getWorldState().getMap());
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "game");
        SoundEffects.getInstance().playStartGameSoundEffect();
        SoundEffects.getInstance().seasonSoundEffect(null, gameState.getEmpireState().getSeason());
    }

    public void multiplayerAction(){
        int option = MultiplayerOption.hostOrJoin();
        if(option==JOptionPane.NO_OPTION){
            MultiplayerOption.joinAddress();
        }else if(option==JOptionPane.YES_OPTION){
            Server server = new Server();
            server.start();
        }
    }

    public void settingsAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "settings");
    }

    public void menuAction(){
        int sure = JOptionPane.showConfirmDialog(menuPanel, "Are you sure ?", "Exit", JOptionPane.YES_OPTION);
        if(sure==JOptionPane.YES_OPTION){
            gamePanel.setAnyPanelOpen(false);
            gamePanel.getPausePanel().close();
            CardLayout cl = (CardLayout) (cards.getLayout());
            cl.show(cards, "menu");
            SoundEffects.getInstance().stopEffects();
        }
    }

    public void continueAction(){
        gamePanel.setAnyPanelOpen(false);
        gamePanel.getPausePanel().changeVisible();
    }

    public void backAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }

    public void exitAction(){
        System.exit(0);
    }

    public void attachPauseAction(){
        if(gamePanel.isAnyPanelOpen())return;
        gamePanel.setAnyPanelOpen(true);
        gamePanel.getPausePanel().setSaveBlocked(
                gamePanel.isCombatActive() ? "Saving is disabled during combat" : null);
        gamePanel.getPausePanel().changeVisible();
    }

}
