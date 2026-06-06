import controller.MenuController;
import view.MenuPanel;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();

            MenuPanel menuPanel = new MenuPanel();
            MenuController menuController = new MenuController(menuPanel);
            menuPanel.draw(frame);
        });
    }
}