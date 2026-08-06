package controller;

import model.state.GameState;
import permanency.SlotID;
import permanency.load.LoadManager;
import permanency.save.MetaDataSave;
import permanency.save.SaveManager;
import view.common.FormatUtils;
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
            if (gamePanel.isCombatActive()) {
                savePanel.setSaveBlocked("Saving is disabled during combat");
            } else {
                savePanel.setSaveBlocked(null);
            }
            CardLayout cl = (CardLayout) (cards.getLayout());
            cl.show(cards, "save");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(savePanel, "Failed to open save panel: " + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            backAction();
        }
    }

    public void backAction() {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "game");
    }

    public void saveSlotAction(SlotID slotId, MetaDataSave existingMeta) {
        if (gamePanel.isCombatActive()) {
            JOptionPane.showMessageDialog(savePanel,
                    "Cannot save during combat. Finish or exit combat first.",
                    "Saving Disabled", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String defaultName = existingMeta != null
                ? existingMeta.getSaveName()
                : FormatUtils.formatSavedAt(Instant.now().toString());
        String saveName = JOptionPane.showInputDialog(savePanel,
                "Enter a name for this save:", defaultName);
        if (saveName == null) {
            return;
        }
        saveName = saveName.trim();
        if (saveName.isEmpty()) {
            JOptionPane.showMessageDialog(savePanel, "Save name cannot be empty.",
                    "Save Name", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (existingMeta != null) {
            int sure = JOptionPane.showConfirmDialog(savePanel,
                    "Slot " + slotId + " already has a save. Overwrite?", "Save",
                    JOptionPane.YES_NO_OPTION);
            if (sure != JOptionPane.YES_OPTION) {
                return;
            }
        }
        try {
            SaveManager.writeSave(gameState, slotId, saveName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(savePanel, "Save failed: " + ex.getMessage()
                            + "\nYour previous save in this slot has been preserved.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(savePanel, "Game Saved!", "Save", JOptionPane.INFORMATION_MESSAGE);
        backAction();
    }
}
