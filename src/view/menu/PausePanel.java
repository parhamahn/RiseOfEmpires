package view.menu;

import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {

    private JButton continueButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton mainMenuButton;
    private JLabel saveBlockedLabel;

    public PausePanel() {

        setOpaque(false);
        setLayout(new GridBagLayout());
        setVisible(false);

        JPanel content = new JPanel();
        content.setOpaque(true);
        content.setBackground(UITheme.PANEL_BG);
        content.setPreferredSize(new Dimension(400, 460));
        content.setBorder(BorderFactory.createLineBorder(UITheme.GOLD, 3));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Paused");
        title.setFont(UITheme.GAME_FONT_BOLD.deriveFont(28f));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 40, 0));
        content.add(title);

        Dimension buttonSize = new Dimension(200, 50);
        float fontSize = 32f;

        this.continueButton = StyledButtons.textButton("Continue", null, buttonSize, fontSize, UITheme.GOLD);
        this.saveButton = StyledButtons.textButton("Save", null, buttonSize, fontSize, UITheme.GOLD);
        this.loadButton = StyledButtons.textButton("Load", null, buttonSize, fontSize, UITheme.GOLD);
        this.mainMenuButton = StyledButtons.textButton("Main Menu", null, buttonSize, fontSize, UITheme.GOLD);

        this.saveBlockedLabel = new JLabel("Saving is disabled during combat");
        saveBlockedLabel.setFont(UITheme.GAME_FONT.deriveFont(18f));
        saveBlockedLabel.setForeground(UITheme.MENU_BUTTON_FG);
        saveBlockedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBlockedLabel.setVisible(false);

        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(continueButton);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(saveButton);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(saveBlockedLabel);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(loadButton);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(mainMenuButton);

        add(content);
    }

    public void setSaveBlocked(String reason) {
        boolean blocked = reason != null;
        saveButton.setEnabled(!blocked);
        saveBlockedLabel.setVisible(blocked);
        if (blocked) {
            saveBlockedLabel.setText(reason);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(UITheme.OVERLAY_BG);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public JButton getContinueButton() {return continueButton;}
    public JButton getSaveButton() {return saveButton;}
    public JButton getLoadButton() {return loadButton;}
    public JButton getMainMenuButton() {return mainMenuButton;}

    public void close() {
        setVisible(false);
    }
    public void open() {
        setVisible(true);
    }
    public void changeVisible(){
        if(isVisible()){close();}else{setVisible(true);}
    }

}
