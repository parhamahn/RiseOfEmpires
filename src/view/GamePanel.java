package view;

import model.Cost;
import model.GameState;
import model.Task;

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

    public void showNotEnoughResources(Cost cost){
        String message = " requires             "+cost.toString();
        JLabel label = new JLabel(message);
        label.setFont(new Font("Pixel Game", Font.PLAIN, 24));
        JOptionPane.showMessageDialog(null,label,"Not Enough Resources",JOptionPane.WARNING_MESSAGE);


    }

    public HUDPanel getHudPanel() { return hudPanel; }
    public MapPanel getMapPanel() { return mapPanel; }
    public UnitListPanel getUnitListPanel() {return unitListPanel;}
    public TaskListPanel getTaskListPanel() { return taskListPanel; }
}