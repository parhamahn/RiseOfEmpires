package view.multiplayer;

import helpers.image.ImageLoader;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LobbyPanel extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(220, 50);

    private JLabel title;
    private JButton exitButton;
    private JButton readyButton;
    private JButton startButton;
    private JComboBox<String> mapSelector;
    private JPanel playerListPanel;
    private JLabel statusLabel;

    public record PlayerRow(String text, boolean ready) {}

    public LobbyPanel() {
        ImageIcon buttonIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", BUTTON_SIZE.width, BUTTON_SIZE.height);

        title = new JLabel("Multiplayer - Lobby");
        title.setFont(UITheme.GAME_FONT_BOLD.deriveFont(40f));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.GAME_FONT);
        statusLabel.setForeground(UITheme.MENU_BUTTON_FG);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);

        playerListPanel = new JPanel();
        playerListPanel.setOpaque(false);
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(playerListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(500, 220));
        scrollPane.setMaximumSize(new Dimension(500, 220));
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);

        mapSelector = new JComboBox<>();
        mapSelector.setMaximumSize(new Dimension(300, 40));
        mapSelector.setAlignmentX(CENTER_ALIGNMENT);

        readyButton = StyledButtons.textButton("Ready", buttonIcon, BUTTON_SIZE, 22f, UITheme.MENU_BUTTON_FG);
        startButton = StyledButtons.textButton("Start Game", buttonIcon, BUTTON_SIZE, 22f, UITheme.MENU_BUTTON_FG);
        exitButton = StyledButtons.textButton("Exit", buttonIcon, BUTTON_SIZE, 22f, UITheme.MENU_BUTTON_FG);
        startButton.setEnabled(false);

        readyButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        exitButton.setAlignmentX(CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.MENU_BG);

        add(Box.createRigidArea(new Dimension(0, 40)));
        add(title);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(statusLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(mapSelector);
        add(Box.createRigidArea(new Dimension(0, 25)));
        add(readyButton);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(startButton);
        add(Box.createVerticalGlue());
        add(exitButton);
        add(Box.createRigidArea(new Dimension(0, 30)));
    }

    public void setPlayerRows(List<PlayerRow> rows) {
        playerListPanel.removeAll();
        for (PlayerRow row : rows) {
            JLabel label = new JLabel(row.text());
            label.setFont(UITheme.GAME_FONT.deriveFont(18f));
            label.setForeground(row.ready() ? UITheme.POSITIVE_CHANGE : Color.WHITE);
            label.setAlignmentX(CENTER_ALIGNMENT);
            playerListPanel.add(label);
            playerListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        playerListPanel.revalidate();
        playerListPanel.repaint();
    }

    public void setStatus(String text) { statusLabel.setText(text); }

    public JLabel getTitle() { return title; }
    public JButton getExitButton() { return exitButton; }
    public JButton getReadyButton() { return readyButton; }
    public JButton getStartButton() { return startButton; }
    public JComboBox<String> getMapSelector() { return mapSelector; }
}