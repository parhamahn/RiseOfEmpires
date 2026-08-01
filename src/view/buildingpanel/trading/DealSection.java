package view.buildingpanel.trading;

import helpers.image.ImageLoader;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class DealSection extends JPanel {

    private final JButton dealButton;
    private final JLabel summaryLabel;

    public DealSection() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        summaryLabel = new JLabel(" ");
        summaryLabel.setFont(UITheme.GAME_FONT.deriveFont(16f));
        summaryLabel.setForeground(Color.WHITE);

        ImageIcon dealIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 200, 56);
        dealButton = StyledButtons.textButton("Deal", dealIcon, new Dimension(200, 56), 18f, UITheme.MENU_BUTTON_FG);
        dealButton.setEnabled(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(summaryLabel, gbc);

        gbc.gridy = 1;
        add(dealButton, gbc);
    }

    public JButton getDealButton() {
        return dealButton;
    }

    public void setSummary(String text) {
        summaryLabel.setText(text == null ? " " : text);
    }
}
