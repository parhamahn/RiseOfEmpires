package view;

import helpers.ImageLoader;
import model.CookQueue;
import model.GameState;
import model.unit.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TownHallPanel extends JPanel {

    private static final Color PANEL_BG = new Color(28, 28, 28);
    private static final Color GOLD = new Color(255, 203, 87);

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
        content.setBackground(PANEL_BG);
        content.setPreferredSize(new Dimension(700, 460));
        content.setBorder(BorderFactory.createLineBorder(GOLD, 3));

        content.add(buildTitleBar(), BorderLayout.NORTH);
        content.add(buildBody(), BorderLayout.CENTER);

        add(content);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private JPanel buildTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel title = new JLabel("Town Hall");
        title.setFont(new Font("Pixel Game", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        titleBar.add(title, BorderLayout.WEST);

        closeButton = new JButton("X");
        closeButton.setFocusable(false);
        closeButton.setFont(new Font("Pixel Game", Font.BOLD, 18));
        closeButton.setBackground(GOLD);
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

        String path = (gameState.getStorageLevel() == 1)
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
        queueLabel.setFont(new Font("Pixel Game", Font.PLAIN, 16));
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
        label.setFont(new Font("Pixel Game", Font.PLAIN, 20));
        label.setForeground(GOLD);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private String unitIconPath(Unit.UNIT_TYPE type) {
        return "/utils/" + type.toString().toLowerCase().replace("_", "") + ".png";
    }

    private JButton createIconButton(String iconPath) {
        JButton button = new JButton();
        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 64);
        button.setIcon(icon);
        if(gameState.getMaxUnit() == gameState.getUnits().size()){
            button.setEnabled(false);
            button.setDisabledIcon(new ImageIcon(
                    GrayFilter.createDisabledImage(icon.getImage())));
        }
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        return button;
    }

    private JButton createRectangularButton(String iconPath, int width, int height) {
        JButton button = new JButton();
        ImageIcon rawIcon = ImageLoader.getInstance().loadIcon(iconPath, -1);

        Image scaled = rawIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaled));

        button.setFocusPainted(false);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);

        return button;
    }

    public void setQueue(List<CookQueue> cookQueues) {
        queuePanel.removeAll();
        for (CookQueue cq : cookQueues) {
            String unitName = cq.getUnit().getUnitType().toString();
            int turns = cq.getTurnToCook()-gameState.getTunrNumber();

            String text;
            text = unitName + " : " + turns + " turn" + (turns > 1 ? "s" : "") + " left";


            JLabel label = new JLabel(text);
            label.setFont(new Font("Pixel Game", Font.PLAIN, 14));
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

    public void refresh() {
        String upgradePath = (gameState.getStorageLevel() == 1)
                ? "/utils/upgrade_storage2.png"
                : "/utils/upgrade_storage3.png";
        if(gameState.getStorageLevel()<3){
            ImageIcon rawIcon = ImageLoader.getInstance().loadIcon(upgradePath, -1);
            Image scaled = rawIcon.getImage().getScaledInstance(200, 60, Image.SCALE_SMOOTH);
            upgradeStorageButton.setIcon(new ImageIcon(scaled));
        }else{
            upgradeStorageButton.setEnabled(false);
        }

        boolean atCap = (gameState.getUnits().size() + gameState.getUnitQueues().size()) >= gameState.getMaxUnit();
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
        setQueue(gameState.getUnitQueues());
    }

}