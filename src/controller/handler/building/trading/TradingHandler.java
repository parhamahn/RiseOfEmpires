package controller.handler.building.trading;

import model.building.TradingBuilding;
import model.state.GameState;
import model.storage.ResourceType;
import model.trade.Trade;
import view.buildingpanel.trading.TradingPanel;
import view.game.GamePanel;

public abstract class TradingHandler {

    protected final GamePanel gamePanel;
    protected final GameState gameState;
    protected TradingBuilding tradingBuilding;

    protected TradingHandler(GamePanel gamePanel, GameState gameState) {
        this.gamePanel = gamePanel;
        this.gameState = gameState;
    }

    protected abstract TradingPanel getPanel();
    protected abstract void setBuildingInstance(TradingBuilding building);

    public void setTradingBuilding(TradingBuilding building) {
        this.tradingBuilding = building;
        setBuildingInstance(building);
    }

    public void attachHandlers() {
        TradingPanel panel = getPanel();

        panel.getCloseButton().addActionListener(e -> {
            gamePanel.setAnyPanelOpen(false);
            panel.close();
        });

        Runnable refreshDealButton = () -> {
            Trade trade = buildTrade();
            panel.getDealSection().getDealButton().setEnabled(trade != null);
            panel.getDealSection().setSummary(trade == null ? null :
                    trade.getToGiveCount() + " " + trade.getToGive() +
                            "  ->  " + trade.getToGetCount() + " " + trade.getToGet());
        };

        panel.getToGiveSection().setOnChange(refreshDealButton);
        panel.getToGetSection().setOnChange(refreshDealButton);
        panel.getQuantitySection().setOnChange(refreshDealButton);

        panel.getDealSection().getDealButton().addActionListener(e -> {
            Trade trade = buildTrade();
            if (trade == null || !trade.isAffordable(gameState.getEmpireState().getStorage())) return;

            trade.deal(gameState.getEmpireState().getStorage());
            tradingBuilding.setUsedThisTurn(true);

            gamePanel.getHudPanel().refresh();
            panel.refresh();
        });
    }

    private Trade buildTrade() {
        TradingPanel panel = getPanel();
        ResourceType give = panel.getToGiveSection().getSelectedResource();
        ResourceType get = panel.getToGetSection().getSelectedResource();
        int qty = panel.getQuantitySection().getSelectedQuantity();

        Trade trade = new Trade(get, give, qty, tradingBuilding.getMultiplier(qty));

        if (!trade.isReady(!tradingBuilding.isUsedThisTurn())) return null;

        return trade;
    }
}
