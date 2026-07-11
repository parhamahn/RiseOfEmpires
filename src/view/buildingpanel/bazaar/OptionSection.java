package view.buildingpanel.bazaar;

import helpers.ImageLoader;
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

        JLabel label = new JLabel(title);
        label.setFont(UITheme.GAME_FONT.deriveFont(48f));
        label.setForeground(UITheme.GOLD);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(CENTER_ALIGNMENT);

        for (int i = 0; i < iconPaths.length; i++) {
            Icon icon = ImageLoader.getInstance().loadIcon(iconPaths[i], iconSize);
            buttons[i] = createCircularButton(icon, buttonSize);
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

        JLabel label = new JLabel(title);
        label.setFont(UITheme.GAME_FONT.deriveFont(48f));
        label.setForeground(UITheme.GOLD);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(CENTER_ALIGNMENT);

        for (int i = 0; i < texts.length; i++) {
            buttons[i] = createCircularTextButton(texts[i], buttonSize);
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

    private JButton createCircularButton(Icon overlayIcon, int size) {
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

    private JButton createCircularTextButton(String text, Dimension size) {
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

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void reset() {
        selectedIndex = -1;
        for (JButton b : buttons) {
            b.setBorderPainted(false);
        }
    }
}
