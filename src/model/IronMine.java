package model;

public class IronMine extends Building{

    private final static Cost cost = new Cost(50, 25, 0, 3);


    public IronMine(Hex hex) {
        super(hex, cost);
    }
}
