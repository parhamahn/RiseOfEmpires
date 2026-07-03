package view;

import model.Cost;
import model.GameState;
import model.Task;
import model.unit.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel {
    private final int FPS = 24;

    private HUDPanel hudPanel;
    private MapPanel mapPanel;
    private UnitListPanel unitListPanel;
    private TaskListPanel taskListPanel;
    private TownHallPanel townHallPanel;
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
            hudPanel.refresh();
        });
        gameLoop.start();

        unitListPanel = new UnitListPanel();
        layeredPane.add(unitListPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = layeredPane.getSize();
                mapPanel.setBounds(0, 0, size.width, size.height);
                hudWrapper.setBounds(0, 0, size.width, size.height);

                int panelWidth = 120;
                unitListPanel.setBounds(size.width - panelWidth, 0, panelWidth, size.height);
            }
        });

        taskListPanel = new TaskListPanel();
        layeredPane.add(taskListPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = layeredPane.getSize();
                taskListPanel.setBounds(0, 0, size.width, size.height);
            }
        });


        townHallPanel = new TownHallPanel();
        layeredPane.add(townHallPanel, JLayeredPane.MODAL_LAYER);
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = layeredPane.getSize();
                townHallPanel.setBounds(0, 0, size.width, size.height);
            }
        });
    }


    public HUDPanel getHudPanel() { return hudPanel; }
    public MapPanel getMapPanel() { return mapPanel; }
    public UnitListPanel getUnitListPanel() {return unitListPanel;}
    public TaskListPanel getTaskListPanel() { return taskListPanel; }
    public TownHallPanel getTownHallPanel() { return townHallPanel; }
}