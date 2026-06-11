import controller.GameController;
import controller.MenuController;
import controller.SettingsController;
import model.Map;
import model.Sound;
import view.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rise of Empires");
            frame.setSize(1400, 900);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            JPanel cards = new JPanel(new CardLayout());
            frame.setContentPane(cards);

            MenuPanel menuPanel = new MenuPanel();
            MenuController menuController = new MenuController(menuPanel, cards);

            Sound music = new Sound();
            music.load("/utils/music.wav");
            music.play();

            SettingsPanel settingsPanel = new SettingsPanel();
            SettingsController settingsController = new SettingsController(settingsPanel, cards);

            Map map = new Map(15, 20);
            HUDPanel hudPanel = new HUDPanel();
            MapPanel mapPanel = new MapPanel(map);
            GamePanel gamePanel = new GamePanel(hudPanel, mapPanel);
            gamePanel.getMapPanel().setMap(map);
            GameController gameController = new GameController(gamePanel, cards);

            cards.add(menuPanel, "menu");
            cards.add(settingsPanel, "settings");
            cards.add(gamePanel, "game");

            frame.setVisible(true);
        });
    }
}