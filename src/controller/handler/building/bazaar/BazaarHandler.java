package controller.handler.building.bazaar;

import model.building.empire.Bazaar;
import model.building.TradingBuilding;
import model.state.GameState;
import controller.handler.building.trading.TradingHandler;
import view.buildingpanel.trading.TradingPanel;
import view.game.GamePanel;

public class BazaarHandler extends TradingHandler {

    private Bazaar bazaar;

    public BazaarHandler(GamePanel gamePanel, GameState gameState) {
        super(gamePanel, gameState);
    }

    @Override
    protected TradingPanel getPanel() {
        return gamePanel.getBazaarPanel();
    }

    @Override
    protected void setBuildingInstance(TradingBuilding building) {
        this.bazaar = (Bazaar) building;
    }

    public void setBazaar(Bazaar bazaar) {
        this.bazaar = bazaar;
        super.setTradingBuilding(bazaar);
    }
}
