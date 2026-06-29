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
            boolean anyMoving = false;
            for (Unit unit : mapPanel.getGameState().getUnits()) {
                if (unit.isMoving()) {
                    boolean arrived = unit.step();
                    if (arrived) {
                        unit.setHex(unit.getDestination());
                        unit.setMoving(false);
                    }
                    anyMoving = true;
                }
            }
            if (anyMoving) repaint();
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
                unitListPanel.setBounds(0, 0, panelWidth, size.height);
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


    }


    public HUDPanel getHudPanel() { return hudPanel; }
    public MapPanel getMapPanel() { return mapPanel; }
    public UnitListPanel getUnitListPanel() {return unitListPanel;}
    public TaskListPanel getTaskListPanel() { return taskListPanel; }
}