package model;

public class Stable extends Building {

    private final static Cost cost = new Cost(30, 10, 0, 2);

    public Stable(Hex hex) {
        super(hex, cost);
    }
}
