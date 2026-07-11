package model.trade;

import model.map.Hex;
import model.storage.ResourceType;
import model.storage.Storage;

public class Trade {

    private ResourceType toGet;
    private int toGetCount;
    private ResourceType toGive;
    private int toGiveCount;
    private double multiplier;

    public Trade(ResourceType toGet, ResourceType toGive, int toGiveCount, double multiplier){
        this.toGet = toGet;
        this.toGive = toGive;
        this.multiplier = multiplier;
        this.toGetCount = (int) Math.floor(toGiveCount * multiplier);
        this.toGiveCount = toGiveCount;
    }

    public ResourceType getToGet() {
        return toGet;
    }

    public void setToGet(ResourceType toGet) {
        this.toGet = toGet;
    }

    public int getToGetCount() {
        return toGetCount;
    }

    public void setToGetCount(int toGetCount) {
        this.toGetCount = toGetCount;
    }

    public ResourceType getToGive() {
        return toGive;
    }

    public void setToGive(ResourceType toGive) {
        this.toGive = toGive;
    }

    public int getToGiveCount() {
        return toGiveCount;
    }

    public void setToGiveCount(int toGiveCount) {
        this.toGiveCount = toGiveCount;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }


    public boolean deal(Storage storage){
        if (storage.getQuantity(toGive) < toGiveCount) return false;
        storage.setQuantity(toGive, storage.getQuantity(toGive) - toGiveCount);
        storage.setQuantity(toGet, Math.min(storage.getMax(toGet), storage.getQuantity(toGet) + this.toGetCount));
        return true;
    }


}