package view.common;

import helpers.ImageLoader;
import model.state.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BaseModalPanel extends JPanel {

    private final GameState gameState;
    private JButton closeButton;
    private BufferedImage panelBackground;

    protected BaseModalPanel(GameState gameState, String title, String backgroundPath) {
        this.gameState = gameState;

        setOpaque(false);
        setLayout(new GridBagLayout());
        setVisible(false);

        this.panelBackground = ImageLoader.getInstance().loadImage(backgroundPath);

        JPanel content = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                if (panelBackground != null) {
                    g2.drawImage(panelBackground, 0, 0, getWidth(), getHeight(), this);
                }
                g2.setColor(UITheme.PANEL_OVERLAY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        content.setOpaque(false);
        content.setPreferredSize(new Dimension(1200, 800));
        content.setBorder(BorderFactory.createLineBorder(UITheme.GOLD, 3));

        content.add(buildTitleBar(title), BorderLayout.NORTH);
        content.add(buildBody(), BorderLayout.CENTER);

        add(content);
    }

    protected abstract JPanel buildBody();

    public abstract void refresh();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(UITheme.OVERLAY_BG);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private JPanel buildTitleBar(String title) {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.GAME_FONT_BOLD.deriveFont(28f));
        titleLabel.setForeground(Color.WHITE);
        titleBar.add(titleLabel, BorderLayout.WEST);

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

    public void open() { refresh(); setVisible(true); }
    public void close() { setVisible(false); }

    public GameState getGameState() { return gameState; }
    public JButton getCloseButton() { return closeButton; }
}
