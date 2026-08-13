package view.common;

import helpers.sound.SoundEffects;

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

    public static void setDisabled(JButton button, boolean disabled, String reason) {
        button.setEnabled(!disabled);
        button.setToolTipText(disabled ? reason : null);
    }

    public static JButton circularButton(Icon overlayIcon, int size) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.drawImage(UITheme.CIRCLE_BUTTON.getImage(), 0, 0, w, h, this);

                if (overlayIcon != null) {
                    int iw = overlayIcon.getIconWidth();
                    int ih = overlayIcon.getIconHeight();
                    g2.drawImage(((ImageIcon) overlayIcon).getImage(), (w - iw) / 2, (h - ih) / 2, this);
                }

                if (isBorderPainted()) {
                    g2.setColor(UITheme.GOLD);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawOval(1, 1, w - 3, h - 3);
                }

                g2.dispose();
            }
        };

        Dimension d = new Dimension(size, size);
        button.setPreferredSize(d);
        button.setMinimumSize(d);
        button.setMaximumSize(d);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setOpaque(false);

        return button;
    }

    public static JButton circularTextButton(String text, Dimension size) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.drawImage(UITheme.CIRCLE_BUTTON.getImage(), 0, 0, w, h, this);

                g2.setFont(getFont());
                g2.setColor(isBorderPainted() ? UITheme.GOLD : UITheme.MENU_BUTTON_FG);
                FontMetrics fm = g2.getFontMetrics();
                String label = getText();
                int tx = (w - fm.stringWidth(label)) / 2;
                int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(label, tx, ty);

                if (isBorderPainted()) {
                    g2.setColor(UITheme.GOLD);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawOval(1, 1, w - 3, h - 3);
                }

                g2.dispose();
            }
        };

        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setOpaque(false);
        button.setFont(UITheme.GAME_FONT_BOLD.deriveFont(24f));

        return button;
    }

    public static JButton textButton(String text, Icon icon, Dimension size, float fontSize, Color fg) {
        JButton button = new JButton(text, icon);

        button.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e) {
                if(button.isEnabled()) SoundEffects.getInstance().playHoverSoundEffect();
            }
        });

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
