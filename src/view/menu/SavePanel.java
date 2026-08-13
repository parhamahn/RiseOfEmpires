package view.menu;

import helpers.image.ImageLoader;
import permanency.SlotID;
import permanency.save.MetaDataSave;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavePanel extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(200, 56);

    private final JPanel listContainer;
    private final Map<SlotID, SaveOption> saveOptions = new HashMap<>();
    private JButton backButton;
    private JLabel blockedLabel;

    public SavePanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.MENU_BG);

        JLabel title = UITheme.createSectionTitle("Save Game");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));

        blockedLabel = new JLabel("", SwingConstants.CENTER);
        blockedLabel.setFont(UITheme.GAME_FONT.deriveFont(20f));
        blockedLabel.setForeground(UITheme.MENU_BUTTON_FG);
        blockedLabel.setVisible(false);
        blockedLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel north = new JPanel();
        north.setOpaque(false);
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.add(title);
        north.add(blockedLabel);

        listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);

        ImageIcon buttonIcon = ImageLoader.getInstance().loadScaledIcon(
                "/utils/menubutton.png", BUTTON_SIZE.width, BUTTON_SIZE.height);
        backButton = StyledButtons.textButton("Back", buttonIcon, BUTTON_SIZE, 24f, UITheme.MENU_BUTTON_FG);
        backButton.setAlignmentX(CENTER_ALIGNMENT);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        bottom.add(backButton);

        add(north, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setSaveBlocked(String reason) {
        boolean blocked = reason != null;
        blockedLabel.setVisible(blocked);
        if (blocked) {
            blockedLabel.setText(reason);
        }
        for (SaveOption option : saveOptions.values()) {
            StyledButtons.setDisabled(option.getSaveButton(), blocked, reason);
        }
    }

    public void init(List<MetaDataSave> metaDataSaves) {
        Map<SlotID, MetaDataSave> metaDataBySlot = new HashMap<>();
        for (MetaDataSave metaDataSave : metaDataSaves) {
            metaDataBySlot.put(metaDataSave.getSlotId(), metaDataSave);
        }

        listContainer.removeAll();
        saveOptions.clear();

        for (SlotID slotId : SlotID.values()) {
            if (slotId == SlotID.AUTO_SAVE) {
                continue;
            }
            SaveOption saveOption = new SaveOption(slotId, metaDataBySlot.get(slotId));
            saveOption.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            saveOptions.put(slotId, saveOption);
            listContainer.add(saveOption);
            listContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    public SaveOption getSaveOption(SlotID slotId) {
        return saveOptions.get(slotId);
    }

    public JButton getBackButton() {
        return backButton;
    }
}
