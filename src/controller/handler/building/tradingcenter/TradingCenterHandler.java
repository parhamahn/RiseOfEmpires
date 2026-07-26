package controller.handler.building.tradingcenter;

import model.building.TradingBuilding;
import model.building.other.TradingCenter;
import model.state.GameState;
import model.storage.ResourceType;
import controller.handler.building.trading.TradingHandler;
import view.buildingpanel.trading.TradingCenterPanel;
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

    @Override
    public void onGiveResourceChanged() {
        TradingCenterPanel panel = (TradingCenterPanel) getPanel();
        ResourceType giveResource = panel.getToGiveSection().getSelectedResource();
        if (giveResource == null) {
            panel.getSliderSection().setMax(0);
        } else {
            panel.getSliderSection().setMax(
                    gameState.getEmpireState().getStorage().getQuantity(giveResource));
        }
    }

    public void setTradingCenter(TradingCenter tradingCenter) {
        this.tradingCenter = tradingCenter;
        super.setTradingBuilding(tradingCenter);
    }
}
