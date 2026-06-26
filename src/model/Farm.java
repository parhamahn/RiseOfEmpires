package model;

public class Farm extends Building{

    private final static Cost cost = new Cost(10, 5, 0, 2);

    public Farm(Hex hex) {
        super(hex, cost);
    }

}
