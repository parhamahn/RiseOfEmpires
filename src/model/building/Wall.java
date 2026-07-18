package model.building;

import model.map.Hex;
import model.map.Map;
import model.storage.Cost;

public class Wall extends EdgeComponent {

    private final static Cost cost = new Cost(0, 5, 0, 0, 2);

    public Wall(Hex hex1, Hex hex2, Map map) {
        super(hex1, hex2, map);
    }

    public static Cost getBuildCost() { return cost; }

    @Override
    public String getImageName() { return "/utils/wallmap.png"; }
}
