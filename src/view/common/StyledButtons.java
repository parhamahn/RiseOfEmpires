package view.common;

import helpers.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class StyledButtons {


    public static JButton iconButton(Icon icon) {
        JButton button = new JButton(icon);
        UITheme.styleIconButton(button);
        return button;
    }

    public static JButton rectangularButton(String iconPath, int width, int height) {
        ImageIcon icon = ImageLoader.getInstance().loadScaledIcon(iconPath, width, height);
        JButton button = new JButton(icon);
        UITheme.styleIconButton(button);
        return button;
    }

    public static JButton textButton(String text, Icon icon, Dimension size, float fontSize, Color fg) {
        JButton button = new JButton(text, icon);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setFont(UITheme.GAME_FONT_BOLD.deriveFont(fontSize));
        button.setForeground(fg);
        button.setFocusable(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    public static JButton redButton(String text, Icon icon, Dimension size, float fontSize) {
        return textButton(text, icon, size, fontSize, new Color(200, 30, 30));
    }
}
