package view.buildingpanel;

import helpers.ImageLoader;
import model.CookQueue;
import model.state.GameState;
import model.unit.Unit;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TownHallPanel extends JPanel {

    private GameState gameState;
    private JButton closeButton;
    private JButton upgradeStorageButton;
    private Map<Unit.UNIT_TYPE, JButton> cookButtons;
    private JPanel queuePanel;


    public TownHallPanel(GameState gameState) {
        this.gameState = gameState;

        setOpaque(false);
        setLayout(new GridBagLayout());
        setVisible(false);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.PANEL_BG);
        content.setPreferredSize(new Dimension(700, 460));
        content.setBorder(BorderFactory.createLineBorder(UITheme.GOLD, 3));

        content.add(buildTitleBar(), BorderLayout.NORTH);
        content.add(buildBody(), BorderLayout.CENTER);

        add(content);
    }


    public JButton getCloseButton() {
        return closeButton;
    }
    public JButton getCookButton(Unit.UNIT_TYPE type) {
        return cookButtons.get(type);
    }
    public Map<Unit.UNIT_TYPE, JButton> getCookButtons() {
        return cookButtons;
    }
    public JButton getUpgradeStorageButton() {
        return upgradeStorageButton;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(UITheme.OVERLAY_BG);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private JPanel buildTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel title = new JLabel("Town Hall");
        title.setFont(UITheme.GAME_FONT_BOLD.deriveFont(28f));
        title.setForeground(Color.WHITE);
        titleBar.add(title, BorderLayout.WEST);

        closeButton = new JButton("X");
        closeButton.setFocusable(false);
        closeButton.setFont(UITheme.GAME_FONT_BOLD.deriveFont(18f));
        closeButton.setBackground(UITheme.GOLD);
        closeButton.setForeground(Color.BLACK);
        closeButton.setPreferredSize(new Dimension(36, 36));
        closeButton.setMaximumSize(new Dimension(36, 36));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        titleBar.add(closeButton, BorderLayout.EAST);

        return titleBar;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new GridLayout(1, 2, 15, 0));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        body.add(buildUpgradeSection());
        body.add(buildCookSection());

        return body;
    }

    private JPanel buildUpgradeSection() {
        JPanel section = new JPanel();
        section.setOpaque(false);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        section.add(sectionLabel("Upgrade Storage"));
        section.add(Box.createRigidArea(new Dimension(0, 15)));

        String path = (gameState.getEmpireState().getStorageLevel() == 1)
                ? "/utils/upgrade_storage2.png"
                : "/utils/upgrade_storage3.png";

        upgradeStorageButton = createRectangularButton(path, 200, 60);

        upgradeStorageButton.setAlignmentX(CENTER_ALIGNMENT);
        section.add(upgradeStorageButton);
        section.add(Box.createVerticalGlue());

        return section;
    }

    private JPanel buildCookSection() {
        JPanel section = new JPanel();
        section.setOpaque(false);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        section.add(sectionLabel("Cook Units"));
        section.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonsRow = new JPanel();
        buttonsRow.setOpaque(false);
        buttonsRow.setLayout(new BoxLayout(buttonsRow, BoxLayout.X_AXIS));
        buttonsRow.setAlignmentX(CENTER_ALIGNMENT);

        cookButtons = new EnumMap<>(Unit.UNIT_TYPE.class);
        for (Unit.UNIT_TYPE type : Unit.UNIT_TYPE.values()) {
            JButton button = createIconButton(unitIconPath(type));
            cookButtons.put(type, button);
            buttonsRow.add(button);
            buttonsRow.add(Box.createRigidArea(new Dimension(8, 0)));
        }

        section.add(buttonsRow);
        section.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel queueLabel = new JLabel("Queue");
        queueLabel.setFont(UITheme.GAME_FONT.deriveFont(16f));
        queueLabel.setForeground(Color.WHITE);
        queueLabel.setAlignmentX(CENTER_ALIGNMENT);
        section.add(queueLabel);
        section.add(Box.createRigidArea(new Dimension(0, 5)));

        queuePanel = new JPanel();
        queuePanel.setOpaque(false);
        queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(queuePanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);

        section.add(scrollPane);

        return section;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UITheme.GAME_FONT.deriveFont(20f));
        label.setForeground(UITheme.GOLD);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private String unitIconPath(Unit.UNIT_TYPE type) {
        return "/utils/" + type.toString().toLowerCase().replace("_", "") + ".png";
    }

    private JButton createIconButton(String iconPath) {
        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 64);
        JButton button = StyledButtons.iconButton(icon);
        if(gameState.getEmpireState().getMaxUnit() == gameState.getEmpireState().getUnits().size()){
            button.setEnabled(false);
            button.setDisabledIcon(new ImageIcon(
                    GrayFilter.createDisabledImage(icon.getImage())));
        }
        return button;
    }

    private JButton createRectangularButton(String iconPath, int width, int height) {
        return StyledButtons.rectangularButton(iconPath, width, height);
    }

    public void setQueue(List<CookQueue> cookQueues) {
        queuePanel.removeAll();
        for (CookQueue cq : cookQueues) {
            String unitName = cq.getUnit().getUnitType().toString();
            int turns = cq.getTurnToCook()-gameState.getWorldState().getTurnNumber();

            String text;
            text = unitName + " : " + turns + " turn" + (turns > 1 ? "s" : "") + " left";


            JLabel label = new JLabel(text);
            label.setFont(UITheme.GAME_FONT_SMALL);
            label.setForeground(turns == 0 ? Color.GREEN : Color.WHITE);
            label.setAlignmentX(CENTER_ALIGNMENT);

            queuePanel.add(label);
            queuePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        }
        queuePanel.revalidate();
        queuePanel.repaint();
    }

    public void open() {
        refresh();
        setVisible(true);
    }

    public void close() {
        setVisible(false);
    }

    public void refresh() {
        String upgradePath = (gameState.getEmpireState().getStorageLevel() == 1)
                ? "/utils/upgrade_storage2.png"
                : "/utils/upgrade_storage3.png";
        if(gameState.getEmpireState().getStorageLevel()<3){
            upgradeStorageButton.setIcon(
                    ImageLoader.getInstance().loadScaledIcon(upgradePath, 200, 60));
        }else{
            upgradeStorageButton.setEnabled(false);
        }

        boolean atCap = (gameState.getEmpireState().getUnits().size() + gameState.getEmpireState().getUnitQueues().size()) >= gameState.getEmpireState().getMaxUnit();
        for (Map.Entry<Unit.UNIT_TYPE, JButton> entry : cookButtons.entrySet()) {
            JButton button = entry.getValue();
            button.setEnabled(!atCap);
            if (atCap) {
                ImageIcon normalIcon = (ImageIcon) button.getIcon();
                if (normalIcon != null) {
                    button.setDisabledIcon(new ImageIcon(
                            GrayFilter.createDisabledImage(normalIcon.getImage())));
                }
            }
        }
        setQueue(gameState.getEmpireState().getUnitQueues());
    }

}
