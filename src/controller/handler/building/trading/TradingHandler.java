package controller.handler.building.trading;

import helpers.sound.SoundEffects;
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
            SoundEffects.getInstance().stopBackgroundEffects();
        });

        Runnable refreshDealButton = () -> {
            Trade trade = buildTrade();
            panel.getDealSection().setDealState(trade != null, trade == null ? buildTradeBlockReason() : null);
            panel.getDealSection().setSummary(trade == null ? null :
                    trade.getToGiveCount() + " " + trade.getToGive() +
                            "  ->  " + trade.getToGetCount() + " " + trade.getToGet());
        };

        Runnable onResourceChange = () -> {
            onGiveResourceChanged();
            refreshDealButton.run();
        };

        panel.getToGiveSection().setOnChange(onResourceChange);
        panel.getToGetSection().setOnChange(refreshDealButton);
        panel.wireQuantityOnChange(refreshDealButton);

        panel.getDealSection().getDealButton().addActionListener(e -> {
            Trade trade = buildTrade();
            if (trade == null || !trade.isAffordable(gameState.getEmpireState().getStorage())) return;

            trade.deal(gameState.getEmpireState().getStorage());
            tradingBuilding.setUsedThisTurn(true);

            gamePanel.getHudPanel().refresh();
            panel.refresh();
        });
    }

    public void onGiveResourceChanged() {
    }

    protected Trade buildTrade() {
        TradingPanel panel = getPanel();
        ResourceType give = panel.getToGiveSection().getSelectedResource();
        ResourceType get = panel.getToGetSection().getSelectedResource();
        int qty = panel.getQuantityValue();

        Trade trade = new Trade(get, give, qty, tradingBuilding.getMultiplier(qty));

        if (!trade.isReady(!tradingBuilding.isUsedThisTurn())) return null;

        return trade;
    }

    protected String buildTradeBlockReason() {
        TradingPanel panel = getPanel();
        ResourceType give = panel.getToGiveSection().getSelectedResource();
        ResourceType get = panel.getToGetSection().getSelectedResource();
        int qty = panel.getQuantityValue();

        if (give == null) return "Select a resource to give";
        if (get == null) return "Select a resource to receive";
        if (qty <= 0) return "Choose a quantity";
        if (tradingBuilding.isUsedThisTurn()) return "This building has already been used this turn";
        if (give == get) return "You can't trade a resource for itself";
        return "Select resources to make a deal";
    }
}
