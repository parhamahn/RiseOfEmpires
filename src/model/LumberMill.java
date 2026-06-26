package model;

public class LumberMill extends Building{

    private final static Cost cost = new Cost(10, 5, 0, 2);

    public LumberMill(Hex hex) {
        super(hex, cost);
    }

}
