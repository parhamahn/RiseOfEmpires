package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuPanel extends JPanel {
    private final Dimension BUTTON_SIZE = new Dimension(200, 50);
    private JLabel titleu;
    private JLabel titlem;
    private JLabel titled;
    private JButton startButton;
    private JButton settingsButton;
    private JButton exitButton;

    public MenuPanel(){
        this.titleu = new JLabel("Rise");
        this.titlem = new JLabel("of");
        this.titled = new JLabel("Empires");
        this.startButton = new JButton("Start");
        this.settingsButton = new JButton("Settings");
        this.exitButton = new JButton("Exit");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(startButton);
        buttons.add(settingsButton);
        buttons.add(exitButton);

        for(JButton button:buttons){
            button.setBackground(new Color(255, 203, 87));
            button.setAlignmentX(CENTER_ALIGNMENT);
            button.setPreferredSize(BUTTON_SIZE);
            button.setMinimumSize(BUTTON_SIZE);
            button.setMaximumSize(BUTTON_SIZE);
            button.setMargin(new Insets(0,0,0,0));
            button.setFont(new Font("Pixel Game", Font.BOLD, 24));
        }

        this.setBackground(new Color(38, 38, 38));

        this.titleu.setForeground(new Color(0, 175, 0));
        this.titleu.setAlignmentX(CENTER_ALIGNMENT);
        this.titlem.setForeground(new Color(255, 255, 255));
        this.titlem.setAlignmentX(CENTER_ALIGNMENT);
        this.titled.setForeground(new Color(255, 0, 0));
        this.titled.setAlignmentX(CENTER_ALIGNMENT);

        titleu.setFont(new Font("Pixel Game", Font.PLAIN, 90));
        titlem.setFont(new Font("Pixel Game", Font.PLAIN, 90));
        titled.setFont(new Font("Pixel Game", Font.BOLD, 90));


        add(Box.createVerticalGlue());
        add(titleu);
        add(titlem);
        add(titled);
        add(Box.createRigidArea(new Dimension(0, 80)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(exitButton);
        add(Box.createVerticalGlue());
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}