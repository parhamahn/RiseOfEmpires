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
        this.soundSlider = new JSlider();
        this.backButton = new JButton("Back");
        this.soundSliderTag = new JLabel("Sound");

        this.title.setFont(new Font("Serif", Font.BOLD, 24));
        this.soundSliderTag.setFont(new Font("Serif", Font.PLAIN, 18));

        this.backButton.setPreferredSize(BUTTON_SIZE);
        this.backButton.setMinimumSize(BUTTON_SIZE);
        this.backButton.setMaximumSize(BUTTON_SIZE);

        soundSlider.setPreferredSize(SLIDER_SIZE);
        soundSlider.setMinimumSize(SLIDER_SIZE);
        soundSlider.setMaximumSize(SLIDER_SIZE);


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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

    public void draw(JFrame frame){
        frame.setContentPane(this);
    }

    public JButton getBackButton() {
        return backButton;
    }
}