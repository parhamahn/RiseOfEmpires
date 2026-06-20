package view;

import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel {
    private final int FPS = 60;

    private HUDPanel hudPanel;
    private MapPanel mapPanel;
    private Timer gameLoop;

    public GamePanel(HUDPanel hudPanel, MapPanel mapPanel) {
        this.hudPanel = hudPanel;
        this.mapPanel = mapPanel;

        setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        layeredPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel hudWrapper = new JPanel(new BorderLayout());
        hudWrapper.setOpaque(false);
        hudWrapper.add(hudPanel, BorderLayout.NORTH);
        layeredPane.add(hudWrapper, JLayeredPane.PALETTE_LAYER);

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = layeredPane.getSize();
                mapPanel.setBounds(0, 0, size.width, size.height);
                hudWrapper.setBounds(0, 0, size.width, size.height);
            }
        });

        gameLoop = new Timer(1000 / FPS, e -> {
            repaint();
        });
        gameLoop.start();
    }

    public void startLoop() { gameLoop.start(); }
    public void stopLoop()  { gameLoop.stop();  }

    public HUDPanel getHudPanel() { return hudPanel; }
    public MapPanel getMapPanel() { return mapPanel; }
}