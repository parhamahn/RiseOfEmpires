package view;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private HUDPanel hudPanel;

    public GamePanel(HUDPanel hudPanel){
        this.hudPanel = hudPanel;

        this.setLayout(new BorderLayout());

        add(hudPanel, BorderLayout.NORTH);
    }

    public HUDPanel getHudPanel() {
        return hudPanel;
    }

}
