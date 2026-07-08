package view.menu;

import helpers.ImageLoader;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(200, 56);
    private final Dimension SLIDER_SIZE = new Dimension(400, 30);
    private JLabel title;
    private JSlider soundSlider;
    private JLabel soundSliderTag;
    private JButton backButton;


    public SettingsPanel(){
        ImageIcon buttonIcon = ImageLoader.getInstance().loadScaledIcon(
                "/utils/menubutton.png", BUTTON_SIZE.width, BUTTON_SIZE.height);

        this.title = new JLabel("Settings");
        this.soundSlider = new JSlider(0, 100, 50);
        this.backButton = StyledButtons.textButton("Back", buttonIcon, BUTTON_SIZE, 24f, UITheme.MENU_BUTTON_FG);
        this.soundSliderTag = new JLabel("Sound");

        this.title.setFont(UITheme.GAME_FONT_BOLD.deriveFont(48f));
        this.title.setForeground(Color.WHITE);
        this.soundSliderTag.setFont(UITheme.GAME_FONT);
        this.soundSliderTag.setForeground(Color.WHITE);

        soundSlider.setPreferredSize(SLIDER_SIZE);
        soundSlider.setMinimumSize(SLIDER_SIZE);
        soundSlider.setMaximumSize(SLIDER_SIZE);
        soundSlider.setBackground(UITheme.MENU_BG);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(UITheme.MENU_BG);
        this.backButton.setAlignmentX(CENTER_ALIGNMENT);
        this.title.setAlignmentX(CENTER_ALIGNMENT);
        this.soundSlider.setAlignmentX(CENTER_ALIGNMENT);
        this.soundSliderTag.setAlignmentX(CENTER_ALIGNMENT);

        add(title);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(soundSliderTag);
        add(soundSlider);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(backButton);
    }


    public JButton getBackButton() {
        return backButton;
    }
    public JSlider getSoundSlider() {
        return soundSlider;
    }
}
