package view;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private HUDPanel hudPanel;
    private MapPanel mapPanel;

    public GamePanel(HUDPanel hudPanel, MapPanel mapPanel){
        this.hudPanel = hudPanel;
        this.mapPanel = mapPanel;

        this.setLayout(new BorderLayout());

        add(hudPanel, BorderLayout.NORTH);
        add(mapPanel, BorderLayout.CENTER);
    }

    public HUDPanel getHudPanel() {
        return hudPanel;
    }

    public MapPanel getMapPanel() {return mapPanel;}

}
