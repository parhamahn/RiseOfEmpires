package view.menu;

import helpers.image.ImageLoader;
import permanency.SlotID;
import permanency.save.MetaDataSave;
import view.common.FormatUtils;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class LoadOption extends JPanel {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 50;
    private static final float FONT_SIZE = 16f;

    private final SlotID slotId;
    private final MetaDataSave metaData;
    private JButton loadButton;
    private JButton removeButton;
    private JLabel summary;

    public LoadOption(SlotID slotId, MetaDataSave metaData) {
        this.slotId = slotId;
        this.metaData = metaData;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);
        init();
        add(summary);
        add(Box.createHorizontalGlue());
        add(loadButton);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(removeButton);
    }

    public void init() {
        int width = 100;
        int height = 50;
        Icon buttonIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", width, height);
        Dimension dimension = new Dimension(width, height);

        summary = new JLabel(buildSummaryText());
        summary.setFont(UITheme.GAME_FONT);
        summary.setForeground(Color.WHITE);

        loadButton = StyledButtons.textButton("Load", buttonIcon, dimension, FONT_SIZE, UITheme.MENU_BUTTON_FG);
        removeButton = StyledButtons.textButton("Remove", buttonIcon, dimension, FONT_SIZE, UITheme.MENU_BUTTON_FG);

        if (metaData == null) {
            summary.setForeground(UITheme.MENU_BUTTON_FG);
            loadButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
    }

    private String buildSummaryText() {
        if (metaData == null) {
            return FormatUtils.formatEnum(slotId.name());
        }
        return FormatUtils.formatEnum(slotId.name()) + " - " + metaData.getSaveName()
                + " | Turn " + metaData.getTurnNumber()
                + " | " + FormatUtils.formatEnum(metaData.getSeason())
                + " | " + FormatUtils.formatEnum(metaData.getTownHallLevel());
    }

    public SlotID getSlotId() {
        return slotId;
    }

    public MetaDataSave getMetaData() {
        return metaData;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JLabel getSummary() {
        return summary;
    }
}
