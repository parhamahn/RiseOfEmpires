package model;

public class StoneMine extends Building{

    private final static Cost cost = new Cost(50, 25, 0, 3);

    public StoneMine(Hex hex) {
        super(hex, cost);
    }

}
