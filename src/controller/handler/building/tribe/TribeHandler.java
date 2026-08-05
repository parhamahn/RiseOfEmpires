package controller.handler.building.tribe;

import model.building.tribes.Tribe;
import model.mission.Mission;
import model.mission.MissionStatus;
import model.state.GameState;
import model.storage.ResourceType;
import model.storage.Storage;
import model.trade.tribe.TribeTrade;
import view.buildingpanel.tribe.GiftPanel;
import view.buildingpanel.tribe.TribeTradePanel;
import view.game.GamePanel;

public class TribeHandler {

    private Tribe tribe;
    private GameState gameState;
    private GamePanel gamePanel;

    public TribeHandler(GameState gameState, GamePanel gamePanel) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
    }

    public void attachHandlers() {

        gamePanel.getTribeMainPanel().getCloseButton().addActionListener(e -> {
            gamePanel.getTribeMainPanel().close();
            gamePanel.setAnyPanelOpen(false);
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getDeclareWar().addActionListener(e -> {
            declareWar(gamePanel.getTribeMainPanel().getTribe());
            gamePanel.getTribeMainPanel().refresh();
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getAskAlliance().addActionListener(e -> {
           askAlliance(gamePanel.getTribeMainPanel().getTribe());
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getAskPeace().addActionListener(e -> {
            askPeace(gamePanel.getTribeMainPanel().getTribe());
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getGetMission().addActionListener(e -> {
           getMission(gamePanel.getTribeMainPanel().getTribe());
        });

        attachGiftHandlers();
        attachTradeHandlers();
    }

    private void attachGiftHandlers() {
        GiftPanel giftPanel = gamePanel.getGiftPanel();
        Storage storage = gameState.getEmpireState().getStorage();

        gamePanel.getTribeMainPanel().getTribeActionSection().getSendGiftButton().addActionListener(e -> {
            gamePanel.getTribeMainPanel().close();
            giftPanel.refresh();
            giftPanel.open();
        });

        giftPanel.getCloseButton().addActionListener(e -> {
            giftPanel.close();
            gamePanel.setAnyPanelOpen(false);
        });

        giftPanel.getResourceSection().setOnChange(() -> giftPanel.updateSliderMax(storage));
        giftPanel.getQuantitySection().setOnChange(() -> giftPanel.updateSummary(storage));

        giftPanel.getSendButton().addActionListener(e -> {
            Tribe current = gamePanel.getTribeMainPanel().getTribe();
            ResourceType resource = giftPanel.getSelectedResource();
            int qty = giftPanel.getQuantity();

            if (resource == null || qty <= 0 || storage.getQuantity(resource) < qty) return;

            sendGift(current, resource, qty);
            gamePanel.getHudPanel().refresh();
            giftPanel.close();
            gamePanel.getTribeMainPanel().open();
            gamePanel.getTribeMainPanel().refresh();
        });
    }

    private void attachTradeHandlers() {
        TribeTradePanel tradePanel = gamePanel.getTribeTradePanel();
        Storage storage = gameState.getEmpireState().getStorage();

        gamePanel.getTribeMainPanel().getTribeActionSection().getStartTrade().addActionListener(e -> {
            Tribe current = gamePanel.getTribeMainPanel().getTribe();
            gamePanel.getTribeMainPanel().close();
            TribeTrade trade = current.getTradeStrategy();
            ResourceType[] provided = trade != null
                    ? trade.getResourceTypes().toArray(ResourceType[]::new)
                    : ResourceType.values();
            tradePanel.setProvidedTypes(provided);
            tradePanel.refresh();
            tradePanel.open();
        });

        tradePanel.getCloseButton().addActionListener(e -> {
            tradePanel.close();
            gamePanel.setAnyPanelOpen(false);
        });

        Runnable onResourceChange = () -> {
            Tribe current = gamePanel.getTribeMainPanel().getTribe();
            tradePanel.updateSliderMax(storage);
            tradePanel.updateSummary(storage, current);
        };
        tradePanel.getToGiveSection().setOnChange(onResourceChange);
        tradePanel.getToGetSection().setOnChange(onResourceChange);
        tradePanel.getQuantitySection().setOnChange(() -> {
            Tribe current = gamePanel.getTribeMainPanel().getTribe();
            tradePanel.updateSummary(storage, current);
        });


        tradePanel.getDealButton().addActionListener(e -> {
            Tribe current = gamePanel.getTribeMainPanel().getTribe();
            ResourceType giveResource = tradePanel.getSelectedGiveResource();
            ResourceType getResource = tradePanel.getSelectedGetResource();
            int qty = tradePanel.getQuantity();

            if (giveResource == null || getResource == null || qty <= 0) return;
            if (storage.getQuantity(giveResource) < qty) return;

            executeTrade(current, giveResource, getResource, qty);
            gamePanel.getHudPanel().refresh();
            tradePanel.close();
            gamePanel.getTribeMainPanel().open();
            gamePanel.getTribeMainPanel().refresh();
        });
    }

    public void sendGift(Tribe tribe, ResourceType resourceType, int amount) {
        gameState.getEmpireState().getStorage().setQuantity(resourceType,
                gameState.getEmpireState().getStorage().getQuantity(resourceType) - amount);
        int relation = GiftPanel.computeRelationGain(resourceType, amount);
        tribe.changeRelation(tribe.getRelation() + relation, gameState.getEmpireState().getAllies());
    }

    public void executeTrade(Tribe tribe, ResourceType giveType, ResourceType getType, int amount) {
        Storage storage = gameState.getEmpireState().getStorage();
        TribeTrade trade = tribe.getTradeStrategy();
        if (trade == null) return;
        int received = (int) Math.floor(amount * trade.getRate());

        storage.setQuantity(giveType, storage.getQuantity(giveType) - amount);
        storage.setQuantity(getType, storage.getQuantity(getType) + received);
        tribe.setUsedThisTurn(true);
    }

    public void declareWar(Tribe tribe) {
        int happinessChange = 0;
        if (tribe.getRelationStatus() == Tribe.RelationStatus.ALLY) happinessChange = -15;
        if (tribe.getRelationStatus() == Tribe.RelationStatus.FRIEND) happinessChange = -5;

        gameState.getEmpireState().setWarHappinessEffect(
                gameState.getEmpireState().getWarHappinessEffect() + happinessChange);

        tribe.changeRelation(-100, gameState.getEmpireState().getAllies());
        for (Mission mission : tribe.getMissions()) {
            mission.setStatus(MissionStatus.CANCELED);
        }
    }

    public void askAlliance(Tribe tribe) {
        tribe.setPermanentReward(true);
        tribe.setRelation(100);
        gameState.getEmpireState().getAllies().add(gamePanel.getTribeMainPanel().getTribe());
    }


    public void askPeace(Tribe tribe) {
        for(ResourceType type : ResourceType.values()){
            if(type==ResourceType.STONE){continue;}
            Storage storage = gameState.getEmpireState().getStorage();
            boolean canAfford = storage.getFoodQuantity() >= 30 && storage.getWoodQuantity() >= 30 && storage.getIronQuantity() >= 30;
            if (!canAfford) return;
            gameState.getEmpireState().getStorage().setQuantity(type, gameState.getEmpireState().getStorage().getQuantity(type)-30);
        }
        tribe.changeRelation(-10, gameState.getEmpireState().getAllies());
    }

    public void getMission(Tribe tribe) {
        for (Mission mission : tribe.getMissions()) {
            if(mission.getStatus() == MissionStatus.AVAILABLE){
                mission.setStatus(MissionStatus.ACTIVE);
                gameState.getEmpireState().getMissions().add(mission);
            }
        }
    }

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
        gamePanel.getTribeMainPanel().setTribe(tribe);
    }

    public GameState getGameState() {
        return gameState;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
