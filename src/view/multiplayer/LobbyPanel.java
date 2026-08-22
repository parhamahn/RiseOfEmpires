package view.multiplayer;

import helpers.image.ImageLoader;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class LobbyPanel extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(200, 56);

    private JLabel title;
    private JButton exitButton;


    public LobbyPanel(){
        ImageIcon buttonIcon = ImageLoader.getInstance().loadScaledIcon(
                "/utils/menubutton.png", BUTTON_SIZE.width, BUTTON_SIZE.height);

        this.title = new JLabel("Multiplayer - Lobby");
        this.title.setFont(UITheme.GAME_FONT_BOLD.deriveFont(48f));
        this.title.setForeground(Color.WHITE);

        this.exitButton = StyledButtons.textButton("Exit", buttonIcon, BUTTON_SIZE, 24f, UITheme.MENU_BUTTON_FG);

        this.title.setAlignmentX(CENTER_ALIGNMENT);
        this.exitButton.setAlignmentX(CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(UITheme.MENU_BG);

        add(title);
        add(Box.createVerticalGlue());
        add(exitButton);
    }


    public JLabel getTitle() {
        return title;
    }

    public void setTitle(JLabel title) {
        this.title = title;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
