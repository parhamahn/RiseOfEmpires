package model;

public abstract class Building {
    private Hex hex;
    private Cost cost;

    protected Building(Hex hex, Cost cost){
        this.hex = hex;
        this.cost = cost;
        hex.setBuilding(this);
    }

    public Hex getHex() {
        return hex;
    }
}
