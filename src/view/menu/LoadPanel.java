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

public class LoadPanel extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(200, 56);
    private final Dimension ROW_MAX_SIZE = new Dimension(Integer.MAX_VALUE, 60);

    private final JPanel listContainer;
    private final Map<SlotID, LoadOption> loadOptions = new HashMap<>();
    private JButton backButton;

    public LoadPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.MENU_BG);

        JLabel title = UITheme.createSectionTitle("Load Game");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));

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

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void init(List<MetaDataSave> metaDataSaves) {
        Map<SlotID, MetaDataSave> metaDataBySlot = new HashMap<>();
        for (MetaDataSave metaDataSave : metaDataSaves) {
            metaDataBySlot.put(metaDataSave.getSlotId(), metaDataSave);
        }

        listContainer.removeAll();
        loadOptions.clear();

        for (SlotID slotId : SlotID.values()) {
            LoadOption loadOption = new LoadOption(slotId, metaDataBySlot.get(slotId));
            loadOption.setMaximumSize(ROW_MAX_SIZE);
            loadOptions.put(slotId, loadOption);
            listContainer.add(loadOption);
            listContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    public LoadOption getLoadOption(SlotID slotId) {
        return loadOptions.get(slotId);
    }

    public JButton getBackButton() {
        return backButton;
    }
}
