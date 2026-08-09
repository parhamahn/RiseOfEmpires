package controller.handler.building.tribe;

import model.building.tribes.BeachTribe;
import model.building.tribes.FarmerTribe;
import model.building.tribes.MountainTribe;
import model.building.tribes.Tribe;
import model.building.tribes.TraderTribe;
import model.building.tribes.WarriorTribe;
import model.mission.Mission;
import model.mission.MissionStatus;
import model.state.GameState;
import model.storage.ResourceType;
import model.storage.Storage;
import model.trade.tribe.TribeTrade;
import model.unit.enemy.Guard;
import view.buildingpanel.tribe.GiftPanel;
import view.buildingpanel.tribe.TribeTradePanel;
import view.game.GamePanel;

import javax.swing.*;

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
            if(JOptionPane.showConfirmDialog(null, "Are you sure you want to declare war with : "+gamePanel.getTribeMainPanel().getTribe().getName()+" ?", "Declare War" , JOptionPane.YES_NO_OPTION)==0){
                declareWar(gamePanel.getTribeMainPanel().getTribe());
                gamePanel.getTribeMainPanel().refresh();
                gamePanel.getHudPanel().refresh();
            }
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getAskAlliance().addActionListener(e -> {
           askAlliance(gamePanel.getTribeMainPanel().getTribe());
           gamePanel.getTribeMainPanel().refresh();
           gamePanel.getHudPanel().refresh();
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getAskPeace().addActionListener(e -> {
            askPeace(gamePanel.getTribeMainPanel().getTribe());
            gamePanel.getTribeMainPanel().refresh();
            gamePanel.getHudPanel().refresh();
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getGetMission().addActionListener(e -> {
           getMission(gamePanel.getTribeMainPanel().getTribe());
           gamePanel.getTribeMainPanel().refresh();
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getDoMission().addActionListener(e -> {
            Tribe current = gamePanel.getTribeMainPanel().getTribe();
            if(current.getActiveMission() == null || !current.getActiveMission().isAffordable(gameState)) return;
            current.getActiveMission().check(gameState);
            current.getActiveMission().processDone(gameState);
            current.setLastMissionTurn(gameState.getWorldState().getTurnNumber());
            current.setActiveMission(null);
            gamePanel.getTribeMainPanel().refresh();
            gamePanel.getHudPanel().refresh();
        });

        gamePanel.getTribeMainPanel().getTribeActionSection().getViewReward().addActionListener(e -> {
            viewReward(gamePanel.getTribeMainPanel().getTribe());
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
        double rate = trade.getRate();
        boolean traderAlly = gameState.getEmpireState().getAllies().stream().anyMatch(TraderTribe.class::isInstance);
        if (traderAlly) rate += 0.2;
        int received = (int) Math.floor(amount * rate);

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
        tribe.setActiveMission(null);
        spawnGuards(tribe);
    }

    private void spawnGuards(Tribe tribe) {
        int currentGuards = (int) tribe.getHex().getUnits().stream().filter(Guard.class::isInstance).count();
        for (int i = currentGuards; i < 2; i++) {
            Guard guard = new Guard(tribe);
            guard.setHex(tribe.getHex());
            tribe.getHex().getUnits().add(guard);
            gameState.getWorldState().getEnemies().add(guard);
        }
    }

    public void askAlliance(Tribe tribe) {
        tribe.setPermanentReward(true);
        tribe.changeRelation(100, gameState.getEmpireState().getAllies());
        if(!gameState.getEmpireState().getAllies().contains(tribe)) gameState.getEmpireState().getAllies().add(tribe);
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
                if(!gameState.getEmpireState().getMissions().contains(mission)){
                    gameState.getEmpireState().getMissions().add(mission);
                }
                if(tribe.getActiveMission() != null){
                    tribe.getActiveMission().setStatus(MissionStatus.CANCELED);
                }
                tribe.setActiveMission(mission);
                tribe.setLastMissionTurn(gameState.getWorldState().getTurnNumber());
            }
        }
    }

    private void viewReward(Tribe tribe) {
        String reward;
        if (tribe instanceof FarmerTribe) {
            reward = "Ally reward : +20% food production\nMission : Send 20 wood and 10 stone | You get 30 food";
        } else if (tribe instanceof MountainTribe) {
            reward = "Ally reward : trade stone/iron at a good rate\nMission : Send 15 wood and 10 iron | You get 20 stone";
        } else if (tribe instanceof BeachTribe) {
            reward = "Ally reward : +4 food production each turn\nMission : Instruct a dock in tribe's area | 30 food + 50% off next dock";
        } else if (tribe instanceof TraderTribe) {
            reward = "Ally reward : +20% trade rate\nMission : Connect a building by roads | +10% trading growth";
        } else if (tribe instanceof WarriorTribe) {
            reward = "Ally reward : a swordsman joins you every 3 turns\nMission : Kill 3 enemies in tribe's area | You get 3 swordsmen";
        } else {
            reward = "";
        }
        String missionLine = tribe.getActiveMission() != null
                ? "\n\nActive mission : " + tribe.getActiveMission().getSummary()
                : "\n\nActive mission : none";
        JOptionPane.showMessageDialog(null, reward + missionLine, tribe.getName() + " - Reward", JOptionPane.INFORMATION_MESSAGE);
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
