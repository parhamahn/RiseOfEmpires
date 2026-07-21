package view.buildingpanel.trading;

import model.state.GameState;
import view.common.UITheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class TradingPanel extends JPanel {

    private final GameState gameState;
    private JButton closeButton;
    private BufferedImage panelBackground;
    private ToGiveSection toGiveSection;
    private ToGetSection toGetSection;
    private QuantitySection quantitySection;
    private DealSection dealSection;

    protected TradingPanel(GameState gameState, String title, String backgroundPath) {
        this.gameState = gameState;

        setOpaque(false);
        setLayout(new GridBagLayout());
        setVisible(false);

        try {
            this.panelBackground = ImageIO.read(getClass().getResource(backgroundPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JPanel content = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                if (panelBackground != null) {
                    g2.drawImage(panelBackground, 0, 0, getWidth(), getHeight(), this);
                }
                g2.setColor(new Color(0, 0, 0, 180));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(UITheme.OVERLAY_BG);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void open() { refresh(); setVisible(true); }
    public void close() { setVisible(false); }

    public void refresh() {
        toGiveSection.reset();
        toGetSection.reset();
        quantitySection.reset();
        dealSection.setSummary(null);
        dealSection.getDealButton().setEnabled(false);
    }

    public GameState getGameState() { return gameState; }
    public JButton getCloseButton() { return closeButton; }
    public ToGiveSection getToGiveSection() { return toGiveSection; }
    public ToGetSection getToGetSection() { return toGetSection; }
    public QuantitySection getQuantitySection() { return quantitySection; }
    public DealSection getDealSection() { return dealSection; }

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

    private JPanel buildBody() {
        JPanel body = new JPanel(new GridLayout(2, 2, 15, 15));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        toGiveSection = new ToGiveSection();
        toGetSection = new ToGetSection();
        quantitySection = new QuantitySection();
        dealSection = new DealSection();

        body.add(toGiveSection);
        body.add(toGetSection);
        body.add(quantitySection);
        body.add(dealSection);

        return body;
    }
}
