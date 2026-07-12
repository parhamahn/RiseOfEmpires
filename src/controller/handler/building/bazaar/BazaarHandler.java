package controller.handler.building.bazaar;

import model.building.Bazaar;
import model.state.GameState;
import model.storage.ResourceType;
import model.trade.Trade;
import view.buildingpanel.bazaar.BazaarPanel;
import view.game.GamePanel;

public class BazaarHandler {

    private final GamePanel gamePanel;
    private final GameState gameState;
    private Bazaar bazaar;

    public BazaarHandler(GamePanel gamePanel, GameState gameState) {
        this.gamePanel = gamePanel;
        this.gameState = gameState;
    }

    public void setBazaar(Bazaar bazaar){this.bazaar = bazaar;}

    public void attachHandlers() {
        BazaarPanel bazaarPanel = gamePanel.getBazaarPanel();

        bazaarPanel.getCloseButton().addActionListener(e -> {gamePanel.setAnyPanelOpen(false); bazaarPanel.close();});

        Runnable refreshDealButton = () ->{
            Trade trade = buildTrade();
            bazaarPanel.getDealSection().getDealButton().setEnabled(trade != null);
            bazaarPanel.getDealSection().setSummary(trade == null ? null :
                    trade.getToGiveCount() + " " + trade.getToGive() +
                            "  ->  " + trade.getToGetCount() + " " + trade.getToGet());
        };

        bazaarPanel.getToGiveSection().setOnChange(refreshDealButton);
        bazaarPanel.getToGetSection().setOnChange(refreshDealButton);
        bazaarPanel.getQuantitySection().setOnChange(refreshDealButton);

        bazaarPanel.getDealSection().getDealButton().addActionListener(e -> {
            Trade trade = buildTrade();
            if (trade == null || !trade.isAffordable(gameState.getEmpireState().getStorage())) return;

            trade.deal(gameState.getEmpireState().getStorage());
            bazaar.setUsedThisTurn(true);

            gamePanel.getHudPanel().refresh();
            bazaarPanel.refresh();
        });
    }

    private Trade buildTrade() {
        BazaarPanel bazaarPanel = gamePanel.getBazaarPanel();
        ResourceType give = bazaarPanel.getToGiveSection().getSelectedResource();
        ResourceType get = bazaarPanel.getToGetSection().getSelectedResource();
        int qty = bazaarPanel.getQuantitySection().getSelectedQuantity();

        Trade trade = new Trade(get, give, qty, Bazaar.multiplierFor(qty));

        if (!trade.isReady(!bazaar.isUsedThisTurn())) return null;

        return trade;
    }

}
