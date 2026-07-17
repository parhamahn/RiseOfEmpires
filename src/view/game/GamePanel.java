package view.game;

import view.buildingpanel.trading.BazaarPanel;
import view.buildingpanel.townhall.TownHallPanel;
import view.buildingpanel.trading.TradingCenterPanel;
import view.game.hud.HUDPanel;
import view.game.lists.TaskListPanel;
import view.game.lists.UnitListPanel;
import view.game.map.MapPanel;
import view.menu.PausePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel {

    private static final int FPS = 30;
    private static final int SIDE_PANEL_WIDTH = 100;

    private HUDPanel hudPanel;
    private MapPanel mapPanel;
    private UnitListPanel unitListPanel;
    private TaskListPanel taskListPanel;
    private TownHallPanel townHallPanel;
    private BazaarPanel bazaarPanel;
    private TradingCenterPanel tradingCenterPanel;
    private JPanel hudWrapper;
    private JLayeredPane layeredPane;
    private PausePanel pausePanel;
    private boolean anyPanelOpen;

    public HUDPanel getHudPanel() { return hudPanel; }
    public MapPanel getMapPanel() { return mapPanel; }
    public UnitListPanel getUnitListPanel() {return unitListPanel;}
    public TaskListPanel getTaskListPanel() { return taskListPanel; }
    public TownHallPanel getTownHallPanel() { return townHallPanel; }
    public BazaarPanel getBazaarPanel() { return bazaarPanel; }
    public TradingCenterPanel getTradingCenterPanel() { return tradingCenterPanel; }
    public PausePanel getPausePanel() { return pausePanel; }
    public boolean isAnyPanelOpen() { return anyPanelOpen; }
    public void setAnyPanelOpen(boolean anyPanelOpen) { this.anyPanelOpen = anyPanelOpen; }


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

        tradingCenterPanel = new TradingCenterPanel(mapPanel.getGameState());
        layeredPane.add(tradingCenterPanel, JLayeredPane.MODAL_LAYER);

        bazaarPanel = new BazaarPanel(mapPanel.getGameState());
        layeredPane.add(bazaarPanel, JLayeredPane.MODAL_LAYER);

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayout();
            }
        });

        pausePanel = new PausePanel();
        layeredPane.add(pausePanel, JLayeredPane.MODAL_LAYER);

        new Timer(1000 / FPS, e -> {
            mapPanel.tick();
        }).start();
    }


    private void relayout() {
        Dimension size = layeredPane.getSize();
        mapPanel.setBounds(0, 0, size.width, size.height);
        hudWrapper.setBounds(0, 0, size.width, size.height);
        unitListPanel.setBounds(size.width - SIDE_PANEL_WIDTH, 0, SIDE_PANEL_WIDTH, size.height);
        taskListPanel.setBounds(0, 0, size.width, size.height);
        townHallPanel.setBounds(0, 0, size.width, size.height);
        bazaarPanel.setBounds(0, 0, size.width, size.height);
        tradingCenterPanel.setBounds(0, 0, size.width, size.height);
        pausePanel.setBounds(0, 0, size.width, size.height);
    }

    public void reset(){
        townHallPanel.close();
        bazaarPanel.close();
        unitListPanel.clear();
        taskListPanel.clear();
    }

}
