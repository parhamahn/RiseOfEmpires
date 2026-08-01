package controller;

import permanency.load.LoadManager;
import permanency.save.MetaDataSave;
import view.menu.LoadPanel;
import view.menu.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoadController {

    LoadPanel loadPanel;
    private JPanel cards;
    private JPanel menuPanel;

    public LoadController(LoadPanel loadPanel, JPanel cards, MenuPanel menuPanel) {
        this.loadPanel = loadPanel;
        this.cards = cards;
        this.menuPanel = menuPanel;
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

    public void loadAction() throws Exception {
        List<MetaDataSave> metaDataSaves = LoadManager.loadAllMetaData();
        loadPanel.init(metaDataSaves);
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "load");
    }
}
