package view.buildingpanel.tribe;

import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class SliderSection extends JPanel {

    private final JSlider slider;
    private final JLabel valueLabel;
    private Runnable onChange;
    private boolean updating;

    public SliderSection(String title) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = UITheme.createSectionTitle(title);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 15)));

        valueLabel = new JLabel("0");

        slider = new JSlider(0, 100, 0);
        slider.setOpaque(false);
        slider.setPreferredSize(new Dimension(400, 50));
        slider.setMaximumSize(new Dimension(400, 50));
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(false);
        slider.setForeground(UITheme.GOLD);
        slider.addChangeListener(e -> {
            if (updating) return;
            valueLabel.setText(String.valueOf(slider.getValue()));
            if (onChange != null) onChange.run();
        });
        add(slider);

        add(Box.createRigidArea(new Dimension(0, 8)));
        valueLabel.setFont(UITheme.GAME_FONT.deriveFont(28f));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(valueLabel);
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    public int getValue() {
        return slider.getValue();
    }

    public void setMax(int max) {
        updating = true;
        slider.setMaximum(max);
        slider.setValue(0);
        valueLabel.setText("0");
        updating = false;
    }

    public void reset() {
        updating = true;
        slider.setValue(0);
        valueLabel.setText("0");
        updating = false;
    }
}
