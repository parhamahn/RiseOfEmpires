package view.common;

import helpers.ImageLoader;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButtons {


    public static JButton iconButton(Icon icon) {
        JButton button = new JButton(icon);
        UITheme.styleIconButton(button);
        return button;
    }

    public static void goldBorder(JButton jButton){
        Border goldenBorder = BorderFactory.createLineBorder(new Color(212, 175, 55), 1);
        jButton.setBorder(goldenBorder);
        jButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                jButton.setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jButton.setBorderPainted(false);
            }
        });
    }

    public static void setToolTip(JButton button, String reason) {
        button.setEnabled(false);
        button.setToolTipText(reason);
    }

    public static JButton textButton(String text, Icon icon, Dimension size, float fontSize, Color fg) {
        JButton button = new JButton(text, icon);
        goldBorder(button);
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

}
