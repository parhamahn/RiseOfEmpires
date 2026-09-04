package view.game;

import view.buildingpanel.trading.BazaarPanel;
import view.buildingpanel.townhall.TownHallPanel;
import view.buildingpanel.trading.TradingCenterPanel;
import view.buildingpanel.tribe.GiftPanel;
import view.buildingpanel.tribe.TribeMainPanel;
import view.buildingpanel.tribe.TribeTradePanel;
import view.combat.CombatMainPanel;
import view.game.hud.HUDPanel;
import view.game.lists.ItemListPanel;
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
    private static final int SIDE_PANEL_HEIGHT = 200;
    public static final int CHAT_WIDTH_HALF = 400;
    public static final int CHAT_HEIGHT = 200;

    private HUDPanel hudPanel;
    private MapPanel mapPanel;
    private UnitListPanel unitListPanel;
    private TaskListPanel taskListPanel;
    private TownHallPanel townHallPanel;
    private BazaarPanel bazaarPanel;
    private TradingCenterPanel tradingCenterPanel;
    private TribeMainPanel tribeMainPanel;
    private GiftPanel giftPanel;
    private TribeTradePanel tribeTradePanel;
    private JPanel hudWrapper;
    private JLayeredPane layeredPane;
    private PausePanel pausePanel;
    private CombatMainPanel combatMainPanel;
    private boolean anyPanelOpen;
    private boolean combatActive;
    private ItemListPanel itemListPanel;
    private ChatSection chatSection;
    private boolean chatOpened;

    public HUDPanel getHudPanel() { return hudPanel; }
    public MapPanel getMapPanel() { return mapPanel; }
    public UnitListPanel getUnitListPanel() {return unitListPanel;}
    public TaskListPanel getTaskListPanel() { return taskListPanel; }
    public ItemListPanel getItemListPanel() { return itemListPanel; }
    public TownHallPanel getTownHallPanel() { return townHallPanel; }
    public BazaarPanel getBazaarPanel() { return bazaarPanel; }
    public TradingCenterPanel getTradingCenterPanel() { return tradingCenterPanel; }
    public PausePanel getPausePanel() { return pausePanel; }
    public boolean isAnyPanelOpen() { return anyPanelOpen; }
    public void setAnyPanelOpen(boolean anyPanelOpen) { this.anyPanelOpen = anyPanelOpen; }
    public boolean isCombatActive() { return combatActive; }
    public void setCombatActive(boolean combatActive) { this.combatActive = combatActive; }
    public TribeMainPanel getTribeMainPanel() { return tribeMainPanel; }
    public GiftPanel getGiftPanel() { return giftPanel; }
    public TribeTradePanel getTribeTradePanel() { return tribeTradePanel; }
    public CombatMainPanel getCombatMainPanel() { return combatMainPanel; }
    public ChatSection getChatSection(){return chatSection;}


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

        itemListPanel = new ItemListPanel();
        layeredPane.add(itemListPanel, JLayeredPane.PALETTE_LAYER);

        townHallPanel = new TownHallPanel(mapPanel.getGameState());
        layeredPane.add(townHallPanel, JLayeredPane.MODAL_LAYER);

        tradingCenterPanel = new TradingCenterPanel(mapPanel.getGameState());
        layeredPane.add(tradingCenterPanel, JLayeredPane.MODAL_LAYER);

        bazaarPanel = new BazaarPanel(mapPanel.getGameState());
        layeredPane.add(bazaarPanel, JLayeredPane.MODAL_LAYER);

        tribeMainPanel = new TribeMainPanel(mapPanel.getGameState());
        layeredPane.add(tribeMainPanel, JLayeredPane.MODAL_LAYER);

        giftPanel = new GiftPanel(mapPanel.getGameState());
        layeredPane.add(giftPanel, JLayeredPane.MODAL_LAYER);

        combatMainPanel = new CombatMainPanel(mapPanel.getGameState());
        layeredPane.add(combatMainPanel, JLayeredPane.MODAL_LAYER);

        tribeTradePanel = new TribeTradePanel(mapPanel.getGameState());
        layeredPane.add(tribeTradePanel, JLayeredPane.MODAL_LAYER);

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayout();
            }
        });

        pausePanel = new PausePanel();
        layeredPane.add(pausePanel, JLayeredPane.MODAL_LAYER);

        chatSection = new ChatSection();
        layeredPane.add(chatSection, JLayeredPane.MODAL_LAYER);

        chatOpened = false;

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
        itemListPanel.setBounds(size.width - SIDE_PANEL_WIDTH, size.height-SIDE_PANEL_HEIGHT, size.width, size.height);
        townHallPanel.setBounds(0, 0, size.width, size.height);
        bazaarPanel.setBounds(0, 0, size.width, size.height);
        tribeMainPanel.setBounds(0, 0, size.width, size.height);
        giftPanel.setBounds(0, 0, size.width, size.height);
        tribeTradePanel.setBounds(0, 0, size.width, size.height);
        tradingCenterPanel.setBounds(0, 0, size.width, size.height);
        combatMainPanel.setBounds(0, 0, size.width, size.height);
        pausePanel.setBounds(0, 0, size.width, size.height);
        chatSection.setBounds(size.width/2 - CHAT_WIDTH_HALF, size.height - CHAT_HEIGHT-10, 2*CHAT_WIDTH_HALF, CHAT_HEIGHT);
    }

    public void reset(){
        townHallPanel.close();
        bazaarPanel.close();
        tribeMainPanel.close();
        giftPanel.close();
        tribeTradePanel.close();
        unitListPanel.clear();
        taskListPanel.clear();
        itemListPanel.clear();
    }

}
