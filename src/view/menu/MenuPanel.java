package view.menu;

import helpers.ImageLoader;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(200, 56);
    private JButton startButton;
    private JButton settingsButton;
    private JButton exitButton;
    private JButton loadButton;
    private BufferedImage backgroundImage;
    private ImageIcon buttonIcon;


    public MenuPanel(){
        buttonIcon = ImageLoader.getInstance().loadScaledIcon(
                "/utils/menubutton.png", BUTTON_SIZE.width, BUTTON_SIZE.height);

        this.startButton = StyledButtons.textButton("New Game", buttonIcon, BUTTON_SIZE, 24f, UITheme.MENU_BUTTON_FG);
        this.settingsButton = StyledButtons.textButton("Settings", buttonIcon, BUTTON_SIZE, 24f, UITheme.MENU_BUTTON_FG);
        this.exitButton = StyledButtons.textButton("Exit", buttonIcon, BUTTON_SIZE, 24f, UITheme.MENU_BUTTON_FG);
        this.loadButton = StyledButtons.textButton("Load", buttonIcon, BUTTON_SIZE, 24f, UITheme.MENU_BUTTON_FG);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (JButton button : new JButton[]{startButton, settingsButton, exitButton, loadButton}) {
            button.setAlignmentX(CENTER_ALIGNMENT);
        }

        this.backgroundImage = ImageLoader.getInstance().loadImage("/utils/menubackground.png");



        add(Box.createRigidArea(new Dimension(0, 500)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(loadButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(exitButton);
        add(Box.createVerticalGlue());
    }


    public JButton getStartButton() {return startButton;}
    public JButton getSettingsButton() {return settingsButton;}
    public JButton getExitButton() {return exitButton;}
    public JButton getLoadButton() {return loadButton;}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);}
    }

}