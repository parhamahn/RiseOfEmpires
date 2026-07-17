package controller.handler.building.tradingcenter;

import model.building.TradingCenter;
import model.state.GameState;
import model.storage.ResourceType;
import model.trade.Trade;
import view.buildingpanel.trading.TradingCenterPanel;
import view.game.GamePanel;

public class TradingCenterHandler {

    private final GamePanel gamePanel;
    private final GameState gameState;
    private TradingCenter tradingCenter;

    public TradingCenterHandler(GamePanel gamePanel, GameState gameState) {
        this.gamePanel = gamePanel;
        this.gameState = gameState;
    }

    public void setTradingCenter(TradingCenter tradingCenter){this.tradingCenter = tradingCenter;}

    public void attachHandlers() {
        TradingCenterPanel tradingCenterPanel = gamePanel.getTradingCenterPanel();

        tradingCenterPanel.getCloseButton().addActionListener(e -> {gamePanel.setAnyPanelOpen(false); tradingCenterPanel.close();});

        Runnable refreshDealButton = () ->{
            Trade trade = buildTrade();
            tradingCenterPanel.getDealSection().getDealButton().setEnabled(trade != null);
            tradingCenterPanel.getDealSection().setSummary(trade == null ? null :
                    trade.getToGiveCount() + " " + trade.getToGive() +
                            "  ->  " + trade.getToGetCount() + " " + trade.getToGet());
        };

        tradingCenterPanel.getToGiveSection().setOnChange(refreshDealButton);
        tradingCenterPanel.getToGetSection().setOnChange(refreshDealButton);
        tradingCenterPanel.getQuantitySection().setOnChange(refreshDealButton);

        tradingCenterPanel.getDealSection().getDealButton().addActionListener(e -> {
            Trade trade = buildTrade();
            if (trade == null || !trade.isAffordable(gameState.getEmpireState().getStorage())) return;

            trade.deal(gameState.getEmpireState().getStorage());
            tradingCenter.setUsedThisTurn(true);

            gamePanel.getHudPanel().refresh();
            tradingCenterPanel.refresh();
        });
    }

    private Trade buildTrade() {
        TradingCenterPanel tradingCenterPanel = gamePanel.getTradingCenterPanel();
        ResourceType give = tradingCenterPanel.getToGiveSection().getSelectedResource();
        ResourceType get = tradingCenterPanel.getToGetSection().getSelectedResource();
        int qty = tradingCenterPanel.getQuantitySection().getSelectedQuantity();

        Trade trade = new Trade(get, give, qty, TradingCenter.getMultiplier());

        if (!trade.isReady(!tradingCenter.isUsedThisTurn())) return null;

        return trade;
    }
}