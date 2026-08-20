package model.trade;

import model.Entity;
import model.Player;
import model.storage.ResourceType;

public class TradeOffer extends Entity {

    private Player fromPlayer;
    private Player toPlayer;
    private ResourceType givenResource;
    private int givenResourceCount;
    private ResourceType ReceivedResource;
    private int ReceivedResourceCount;

    public TradeOffer(){
        super();
    }

    public Player getFromPlayer() {
        return fromPlayer;
    }

    public void setFromPlayer(Player fromPlayer) {
        this.fromPlayer = fromPlayer;
    }

    public Player getToPlayer() {
        return toPlayer;
    }

    public void setToPlayer(Player toPlayer) {
        this.toPlayer = toPlayer;
    }

    public ResourceType getGivenResource() {
        return givenResource;
    }

    public void setGivenResource(ResourceType givenResource) {
        this.givenResource = givenResource;
    }

    public int getGivenResourceCount() {
        return givenResourceCount;
    }

    public void setGivenResourceCount(int givenResourceCount) {
        this.givenResourceCount = givenResourceCount;
    }

    public ResourceType getReceivedResource() {
        return ReceivedResource;
    }

    public void setReceivedResource(ResourceType receivedResource) {
        ReceivedResource = receivedResource;
    }

    public int getReceivedResourceCount() {
        return ReceivedResourceCount;
    }

    public void setReceivedResourceCount(int receivedResourceCount) {
        ReceivedResourceCount = receivedResourceCount;
    }
}
