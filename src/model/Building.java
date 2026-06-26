package model;

public abstract class Building {
    private Hex hex;
    private Cost cost;

    public Building(Hex hex, Cost cost){
        this.hex = hex;
        this.cost = cost;
    }

    public Hex getHex() {
        return hex;
    }

    public Cost getCost(){
        return cost;
    }

}
