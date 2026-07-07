package view.menu;

import helpers.ImageLoader;
import view.common.StyledButtons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuPanel extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(200, 56);
    private JButton startButton;
    private JButton settingsButton;
    private JButton exitButton;
    private BufferedImage backgroundImage;
    private ImageIcon buttonIcon;


    public MenuPanel(){
        buttonIcon = ImageLoader.getInstance().loadScaledIcon(
                "/utils/menubutton.png", BUTTON_SIZE.width, BUTTON_SIZE.height);

        this.startButton = StyledButtons.menuButton("Start", buttonIcon, BUTTON_SIZE);
        this.settingsButton = StyledButtons.menuButton("Settings", buttonIcon, BUTTON_SIZE);
        this.exitButton = StyledButtons.menuButton("Exit", buttonIcon, BUTTON_SIZE);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (JButton button : new JButton[]{startButton, settingsButton, exitButton}) {
            button.setAlignmentX(CENTER_ALIGNMENT);
        }

        try {
            this.backgroundImage = ImageIO.read(getClass().getResource("/utils/menubackground.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Resource not found on classpath", e);
        }



        add(Box.createRigidArea(new Dimension(0, 500)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(exitButton);
        add(Box.createVerticalGlue());
    }


    public JButton getStartButton() {return startButton;}
    public JButton getSettingsButton() {return settingsButton;}
    public JButton getExitButton() {return exitButton;}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);}
    }

}