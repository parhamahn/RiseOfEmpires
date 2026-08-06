package controller;

import model.state.GameState;
import permanency.SlotID;
import permanency.load.LoadManager;
import permanency.load.MigrationManager;
import permanency.save.MetaDataSave;
import permanency.save.SaveManager;
import view.game.GamePanel;
import view.menu.LoadOption;
import view.menu.LoadPanel;
import view.menu.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoadController {

    LoadPanel loadPanel;
    private JPanel cards;
    private JPanel menuPanel;
    private GameState gameState;
    private GamePanel gamePanel;
    private SelectionContext selectionContext;
    private String backTarget = "menu";

    public LoadController(GameState gameState, GamePanel gamePanel, SelectionContext selectionContext, LoadPanel loadPanel, JPanel cards, MenuPanel menuPanel) {
        this.loadPanel = loadPanel;
        this.cards = cards;
        this.menuPanel = menuPanel;
        this.gameState = gameState;
        this.selectionContext = selectionContext;
        this.gamePanel = gamePanel;
        loadPanel.getBackButton().addActionListener(e -> {
            backAction();
        });
        menuPanel.getLoadButton().addActionListener(e -> {
            backTarget = "menu";
            loadAction();
        });
        gamePanel.getPausePanel().getLoadButton().addActionListener(e -> {
            backTarget = "game";
            loadAction();
        });
    }

    public void backAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, backTarget);
    }

    public void removeAction(MetaDataSave metaDataSave) {
        try {
            LoadManager.deleteData(metaDataSave);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(loadPanel, "Could not delete save: " + ex.getMessage(),
                    "Remove Error", JOptionPane.ERROR_MESSAGE);
        }
        loadAction();
    }

    public void loadAction() {
        List<MetaDataSave> metaDataSaves;
        try {
            metaDataSaves = LoadManager.loadAllMetaData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(loadPanel, "Failed to load saves: " + ex.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
            backAction();
            return;
        }
        loadPanel.init(metaDataSaves);
        for(SlotID slotID:SlotID.values()){
            LoadOption option = loadPanel.getLoadOption(slotID);
            if(option == null){continue;}
            option.getLoadButton().addActionListener(e -> loadSlotAction(slotID, option.getMetaData()));
            option.getRemoveButton().addActionListener(e -> removeAction(option.getMetaData()));
        }
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "load");
    }

    public void loadSlotAction(SlotID slotId, MetaDataSave meta) {
        if (meta == null) {
            JOptionPane.showMessageDialog(loadPanel, "No save in this slot.", "Load",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (LoadManager.isTooNew(meta)) {
            JOptionPane.showMessageDialog(loadPanel,
                    "This save was created by a newer version of the game and cannot be loaded.",
                    "Incompatible Save", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (LoadManager.needsMigration(meta)) {
            try {
                MigrationManager.migrate(meta);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loadPanel,
                        "Could not migrate this old save: " + ex.getMessage(),
                        "Migration Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        GameState loaded;
        try {
            loaded = LoadManager.loadData(meta);
        } catch (Exception ex) {
            handleCorruptedSave(meta);
            return;
        }

        gameState.loadFrom(loaded);
        gamePanel.reset();
        gamePanel.getMapPanel().setMap(gameState.getWorldState().getMap());
        gamePanel.getHudPanel().refresh();
        selectionContext.reset();
        if (gamePanel.getPausePanel().isVisible()) {
            gamePanel.getPausePanel().close();
        }
        gamePanel.setAnyPanelOpen(false);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, "game");
    }

    private void handleCorruptedSave(MetaDataSave meta) {
        Object[] options = {"Delete this save", "Load last autosave", "Cancel"};
        int choice = JOptionPane.showOptionDialog(loadPanel,
                "This save is corrupted and cannot be loaded.",
                "Corrupted Save", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE, null, options, options[2]);
        if (choice == 0) {
            removeAction(meta);
        } else if (choice == 1) {
            loadAutosave();
        }
    }

    private void loadAutosave() {
        try {
            List<MetaDataSave> metaDataSaves = LoadManager.loadAllMetaData();
            MetaDataSave auto = null;
            for (MetaDataSave metaDataSave : metaDataSaves) {
                if (metaDataSave.getSlotId() == SlotID.AUTO_SAVE) {
                    auto = metaDataSave;
                    break;
                }
            }
            if (auto == null) {
                JOptionPane.showMessageDialog(loadPanel, "No autosave available.", "Load Autosave",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            loadSlotAction(SlotID.AUTO_SAVE, auto);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(loadPanel, "Failed to load autosave: " + ex.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
