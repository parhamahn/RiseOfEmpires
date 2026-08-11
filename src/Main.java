import controller.*;
import helpers.sound.Sound;
import model.map.Map;
import model.product.Production;
import model.storage.Storage;
import model.state.GameState;
import view.game.GamePanel;
import view.game.hud.HUDPanel;
import view.game.map.MapPanel;
import view.menu.LoadPanel;
import view.menu.MenuPanel;
import view.menu.SavePanel;
import view.menu.SettingsPanel;
import javax.swing.*;
import java.awt.*;

public class Main {

    private static Sound music;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rise of Empires");
            frame.setSize(1920, 1080);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel cards = new JPanel(new CardLayout());
            frame.setContentPane(cards);

            Storage storage = new Storage();
            Production production = new Production();
            GameState gameState = new GameState(storage, production);
            SelectionContext selectionContext = new SelectionContext();
            Map map = gameState.getWorldState().getMap();


            music = new Sound();
            music.load("/utils/music.wav");
            music.play();

            SettingsPanel settingsPanel = new SettingsPanel();
            SettingsController settingsController = new SettingsController(settingsPanel, cards);

            HUDPanel hudPanel = new HUDPanel(gameState);
            MapPanel mapPanel = new MapPanel(map, gameState, selectionContext);
            GamePanel gamePanel = new GamePanel(hudPanel, mapPanel);
            gamePanel.getMapPanel().setMap(map);

            GameController gameController = new GameController(gamePanel, cards, gameState, selectionContext);

            MenuPanel menuPanel = new MenuPanel();
            MenuController menuController = new MenuController(menuPanel, cards, gameState, gamePanel, selectionContext);

            LoadPanel loadPanel = new LoadPanel();
            LoadController loadController = new LoadController(gameState, gamePanel, selectionContext, loadPanel, cards, menuPanel);

            SavePanel savePanel = new SavePanel();
            SaveController saveController = new SaveController(gameState, gamePanel, savePanel, cards);

            cards.add(menuPanel, "menu");
            cards.add(settingsPanel, "settings");
            cards.add(gamePanel, "game");
            cards.add(loadPanel, "load");
            cards.add(savePanel, "save");

            frame.setVisible(true);
        });
    }
}
