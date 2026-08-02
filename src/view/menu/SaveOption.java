package view.menu;

import helpers.image.ImageLoader;
import permanency.SlotID;
import permanency.save.MetaDataSave;
import view.common.FormatUtils;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class SaveOption extends JPanel {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 50;
    private static final float FONT_SIZE = 16f;

    private final SlotID slotId;
    private final MetaDataSave metaData;
    private JButton saveButton;
    private JLabel summary;

    public SaveOption(SlotID slotId, MetaDataSave metaData) {
        this.slotId = slotId;
        this.metaData = metaData;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);
        init();
        add(summary);
        add(Box.createHorizontalGlue());
        add(saveButton);
    }

    public void init() {
        Icon buttonIcon = ImageLoader.getInstance().loadScaledIcon(
                "/utils/menubutton.png", BUTTON_WIDTH, BUTTON_HEIGHT);
        Dimension dimension = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        summary = new JLabel(buildSummaryText());
        summary.setFont(UITheme.GAME_FONT);
        summary.setForeground(Color.WHITE);

        saveButton = StyledButtons.textButton("Save", buttonIcon, dimension, FONT_SIZE, UITheme.MENU_BUTTON_FG);

        if (metaData == null) {
            summary.setForeground(UITheme.MENU_BUTTON_FG);
        }
    }

    private String buildSummaryText() {
        if (metaData == null) {
            return FormatUtils.formatEnum(slotId.name()) + " - Empty";
        }
        return metaData.getSummary();
    }

    public SlotID getSlotId() {
        return slotId;
    }

    public MetaDataSave getMetaData() {
        return metaData;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JLabel getSummary() {
        return summary;
    }
}
