import controller.GameController;
import controller.MenuController;
import controller.SettingsController;
import helpers.Sound;
import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static Sound music;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rise of Empires");
            frame.setSize(1400, 900);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            JPanel cards = new JPanel(new CardLayout());
            frame.setContentPane(cards);

            Storage storage = new Storage();
            Production production = new Production();
            Map map = new Map(15, 20);
            GameState gameState = new GameState(storage, production, map);

            MenuPanel menuPanel = new MenuPanel();
            MenuController menuController = new MenuController(menuPanel, cards);

            music = new Sound();
            music.load("/utils/music.wav");
            music.play();

            SettingsPanel settingsPanel = new SettingsPanel();
            SettingsController settingsController = new SettingsController(settingsPanel, cards);

            HUDPanel hudPanel = new HUDPanel(gameState);
            MapPanel mapPanel = new MapPanel(map, gameState);
            GamePanel gamePanel = new GamePanel(hudPanel, mapPanel);
            gamePanel.getMapPanel().setMap(map);

            GameController gameController = new GameController(gamePanel, cards, gameState);

            cards.add(menuPanel, "menu");
            cards.add(settingsPanel, "settings");
            cards.add(gamePanel, "game");

            frame.setVisible(true);
        });
    }
}