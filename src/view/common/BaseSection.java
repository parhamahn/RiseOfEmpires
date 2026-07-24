package view.common;

import javax.swing.*;
import java.awt.*;

public abstract class BaseSection extends JPanel {

    protected BaseSection(String title, int gapAfterTitle) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = UITheme.createSectionTitle(title);
        add(label);
        add(Box.createRigidArea(new Dimension(0, gapAfterTitle)));
    }

    protected BaseSection(String title, int gapAfterTitle, int Layout) {
        setOpaque(false);
        setLayout(new BoxLayout(this, Layout));

        JLabel label = UITheme.createSectionTitle(title);
        add(label);
        add(Box.createRigidArea(new Dimension(0, gapAfterTitle)));
    }

    protected BaseSection(String title) {
        this(title, 15);
    }
}
