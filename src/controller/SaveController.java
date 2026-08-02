package controller;

import model.state.GameState;
import permanency.SlotID;
import permanency.load.LoadManager;
import permanency.save.MetaDataSave;
import permanency.save.SaveManager;
import view.game.GamePanel;
import view.menu.SaveOption;
import view.menu.SavePanel;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.List;

public class SaveController {

    private final SavePanel savePanel;
    private final JPanel cards;
    private final GameState gameState;
    private final GamePanel gamePanel;

    public SaveController(GameState gameState, GamePanel gamePanel, SavePanel savePanel, JPanel cards) {
        this.savePanel = savePanel;
        this.cards = cards;
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        gamePanel.getPausePanel().getSaveButton().addActionListener(e -> openSavePanel());
        savePanel.getBackButton().addActionListener(e -> backAction());
    }

    public void openSavePanel() {
        try {
            List<MetaDataSave> metaDataSaves = LoadManager.loadAllMetaData();
            savePanel.init(metaDataSaves);
            for (SlotID slotID : SlotID.values()) {
                SaveOption option = savePanel.getSaveOption(slotID);
                if (option == null) {
                    continue;
                }
                option.getSaveButton().addActionListener(e -> saveSlotAction(slotID, option.getMetaData()));
            }
            CardLayout cl = (CardLayout) (cards.getLayout());
            cl.show(cards, "save");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void backAction() {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "game");
    }

    public void saveSlotAction(SlotID slotId, MetaDataSave existingMeta) {
        if (existingMeta != null) {
            int sure = JOptionPane.showConfirmDialog(savePanel,
                    "Slot " + slotId + " already has a save. Overwrite?", "Save",
                    JOptionPane.YES_NO_OPTION);
            if (sure != JOptionPane.YES_OPTION) {
                return;
            }
        }
        try {
            SaveManager.writeSave(gameState, slotId, Instant.now().toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        JOptionPane.showMessageDialog(savePanel, "Game Saved!", "Save", JOptionPane.INFORMATION_MESSAGE);
        backAction();
    }
}
