package controller;

import model.state.GameState;
import permanency.SlotID;
import permanency.load.LoadManager;
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
            try {
                loadAction();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void backAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }

    public void removeAction(MetaDataSave metaDataSave) throws Exception {
        LoadManager.deleteData(metaDataSave);
        loadAction();
    }

    public void loadAction() throws Exception {
        List<MetaDataSave> metaDataSaves = LoadManager.loadAllMetaData();
        loadPanel.init(metaDataSaves);
        for(SlotID slotID:SlotID.values()){
            LoadOption option = loadPanel.getLoadOption(slotID);
            if(option == null){continue;}
            option.getLoadButton().addActionListener(e -> {
                try {
                    loadSlotAction(slotID, option.getMetaData());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            option.getRemoveButton().addActionListener(e -> {
                try {
                    removeAction(option.getMetaData());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "load");
    }

    public void loadSlotAction(SlotID slotId, MetaDataSave meta) throws Exception {
        GameState loaded = LoadManager.loadData(meta);
        gameState.loadFrom(loaded);
        gamePanel.reset();
        gamePanel.getMapPanel().setMap(gameState.getWorldState().getMap());
        gamePanel.getHudPanel().refresh();
        selectionContext.reset();
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, "game");
    }
}
