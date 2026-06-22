package view;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private final Dimension BUTTON_SIZE = new Dimension(120, 40);
    private final Dimension SLIDER_SIZE = new Dimension(400, 30);
    private JLabel title;
    private JSlider soundSlider;
    private JLabel soundSliderTag;
    private JButton backButton;

    public SettingsPanel(){
        this.title = new JLabel("Settings");
        this.soundSlider = new JSlider(0, 100, 50);
        this.backButton = new JButton("Back");
        this.soundSliderTag = new JLabel("Sound");

        this.title.setFont(new Font("Pixel Game", Font.BOLD, 48));
        this.title.setForeground(Color.WHITE);
        this.soundSliderTag.setFont(new Font("Pixel Game", Font.PLAIN, 24));
        this.soundSliderTag.setForeground(Color.WHITE);

        this.backButton.setPreferredSize(BUTTON_SIZE);
        this.backButton.setMinimumSize(BUTTON_SIZE);
        this.backButton.setMaximumSize(BUTTON_SIZE);
        this.backButton.setBackground(new Color(255, 203, 87));
        this.backButton.setFont(new Font("Pixel Game", Font.PLAIN, 24));
        this.backButton.setFocusable(false);

        soundSlider.setPreferredSize(SLIDER_SIZE);
        soundSlider.setMinimumSize(SLIDER_SIZE);
        soundSlider.setMaximumSize(SLIDER_SIZE);
        soundSlider.setBackground(new Color(38, 38, 38));


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(38, 38, 38));
        this.backButton.setAlignmentX(CENTER_ALIGNMENT);
        this.title.setAlignmentX(CENTER_ALIGNMENT);
        this.soundSlider.setAlignmentX(CENTER_ALIGNMENT);
        this.soundSliderTag.setAlignmentX(CENTER_ALIGNMENT);

        Box.createVerticalGlue();
        add(title);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(soundSliderTag);
        add(soundSlider);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(backButton);
        Box.createVerticalGlue();
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JSlider getSoundSlider() {
        return soundSlider;
    }
}
