package view.buildingpanel.trading;

import helpers.image.ImageLoader;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class OptionSection extends JPanel {

    private int selectedIndex = -1;
    private final JButton[] buttons;
    private Runnable onChange;

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    public OptionSection(String title, String[] iconPaths, int buttonSize, int iconSize) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        buttons = new JButton[iconPaths.length];

        JLabel label = UITheme.createSectionTitle(title);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(CENTER_ALIGNMENT);

        for (int i = 0; i < iconPaths.length; i++) {
            Icon icon = ImageLoader.getInstance().loadIcon(iconPaths[i], iconSize);
            buttons[i] = StyledButtons.circularButton(icon, buttonSize);
            final int idx = i;
            buttons[i].addActionListener(e -> select(idx));
            buttonRow.add(buttons[i]);
        }

        add(buttonRow);
    }

    public OptionSection(String title, String[] texts, Dimension buttonSize) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        buttons = new JButton[texts.length];

        JLabel label = UITheme.createSectionTitle(title);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(CENTER_ALIGNMENT);

        for (int i = 0; i < texts.length; i++) {
            buttons[i] = StyledButtons.circularTextButton(texts[i], buttonSize);
            final int idx = i;
            buttons[i].addActionListener(e -> select(idx));
            buttonRow.add(buttons[i]);
        }

        add(buttonRow);
    }

    private void select(int index) {
        selectedIndex = index;
        for (JButton b : buttons) {
            b.setBorderPainted(false);
        }
        buttons[index].setBorderPainted(true);
        if (onChange != null) onChange.run();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setItemEnabled(int index, boolean enabled) {
        buttons[index].setEnabled(enabled);
    }

    public void reset() {
        selectedIndex = -1;
        for (JButton b : buttons) {
            b.setBorderPainted(false);
            b.setEnabled(true);
        }
    }
}
