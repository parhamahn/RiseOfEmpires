package view;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final Dimension BUTTON_SIZE = new Dimension(200, 50);
    private JLabel title;
    private JButton startButton;
    private JButton settingsButton;
    private JButton exitButton;

    public MenuPanel(){
        this.title = new JLabel("Rise of Empires");
        this.startButton = new JButton("Start");
        this.settingsButton = new JButton("Settings");
        this.exitButton = new JButton("Exit");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.startButton.setAlignmentX(CENTER_ALIGNMENT);
        this.title.setAlignmentX(CENTER_ALIGNMENT);
        this.exitButton.setAlignmentX(CENTER_ALIGNMENT);
        this.settingsButton.setAlignmentX(CENTER_ALIGNMENT);

        title.setFont(new Font("Serif", Font.BOLD, 36));

        this.startButton.setPreferredSize(BUTTON_SIZE);
        this.startButton.setMinimumSize(BUTTON_SIZE);
        this.startButton.setMaximumSize(BUTTON_SIZE);

        this.settingsButton.setPreferredSize(BUTTON_SIZE);
        this.settingsButton.setMinimumSize(BUTTON_SIZE);
        this.settingsButton.setMaximumSize(BUTTON_SIZE);

        this.exitButton.setPreferredSize(BUTTON_SIZE);
        this.exitButton.setMinimumSize(BUTTON_SIZE);
        this.exitButton.setMaximumSize(BUTTON_SIZE);

        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(exitButton);
        add(Box.createVerticalGlue());
    }

    public void draw(JFrame frame){
        frame.setContentPane(this);
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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