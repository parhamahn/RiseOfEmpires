package model;

public class Town extends Building{

    private final static Cost cost = new Cost(50, 25, 0, 3);

    public Town(Hex hex) {
        super(hex, cost);
    }
}
