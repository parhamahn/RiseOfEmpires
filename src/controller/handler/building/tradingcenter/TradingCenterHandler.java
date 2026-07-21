package controller.handler.building.tradingcenter;

import model.building.TradingBuilding;
import model.building.other.TradingCenter;
import model.state.GameState;
import controller.handler.building.trading.TradingHandler;
import view.buildingpanel.trading.TradingPanel;
import view.game.GamePanel;

public class TradingCenterHandler extends TradingHandler {

    private TradingCenter tradingCenter;

    public TradingCenterHandler(GamePanel gamePanel, GameState gameState) {
        super(gamePanel, gameState);
    }

    @Override
    protected TradingPanel getPanel() {
        return gamePanel.getTradingCenterPanel();
    }

    @Override
    protected void setBuildingInstance(TradingBuilding building) {
        this.tradingCenter = (TradingCenter) building;
    }

    public void setTradingCenter(TradingCenter tradingCenter) {
        this.tradingCenter = tradingCenter;
        super.setTradingBuilding(tradingCenter);
    }
}
