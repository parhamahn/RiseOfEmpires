package view.game;

import view.buildingpanel.TownHallPanel;
import view.game.hud.HUDPanel;
import view.game.lists.TaskListPanel;
import view.game.lists.UnitListPanel;
import view.game.map.MapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel {

    private static final int FPS = 24;
    private static final int SIDE_PANEL_WIDTH = 120;

    private HUDPanel hudPanel;
    private MapPanel mapPanel;
    private UnitListPanel unitListPanel;
    private TaskListPanel taskListPanel;
    private TownHallPanel townHallPanel;
    private JPanel hudWrapper;
    private JLayeredPane layeredPane;

    public HUDPanel getHudPanel() { return hudPanel; }
    public MapPanel getMapPanel() { return mapPanel; }
    public UnitListPanel getUnitListPanel() {return unitListPanel;}
    public TaskListPanel getTaskListPanel() { return taskListPanel; }
    public TownHallPanel getTownHallPanel() { return townHallPanel; }


    public GamePanel(HUDPanel hudPanel, MapPanel mapPanel) {
        this.hudPanel = hudPanel;
        this.mapPanel = mapPanel;

        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        layeredPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER);

        hudWrapper = new JPanel(new BorderLayout());
        hudWrapper.setOpaque(false);
        hudWrapper.add(hudPanel, BorderLayout.NORTH);
        layeredPane.add(hudWrapper, JLayeredPane.PALETTE_LAYER);

        unitListPanel = new UnitListPanel();
        layeredPane.add(unitListPanel, JLayeredPane.PALETTE_LAYER);

        taskListPanel = new TaskListPanel();
        layeredPane.add(taskListPanel, JLayeredPane.PALETTE_LAYER);

        townHallPanel = new TownHallPanel(mapPanel.getGameState());
        layeredPane.add(townHallPanel, JLayeredPane.MODAL_LAYER);

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayout();
            }
        });

        new Timer(1000 / FPS, e -> {
            mapPanel.tick();
            repaint();
            hudPanel.refresh();
        }).start();
    }


    private void relayout() {
        Dimension size = layeredPane.getSize();
        mapPanel.setBounds(0, 0, size.width, size.height);
        hudWrapper.setBounds(0, 0, size.width, size.height);
        unitListPanel.setBounds(size.width - SIDE_PANEL_WIDTH, 0, SIDE_PANEL_WIDTH, size.height);
        taskListPanel.setBounds(0, 0, size.width, size.height);
        townHallPanel.setBounds(0, 0, size.width, size.height);
    }
}
