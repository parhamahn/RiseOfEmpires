package model.building.edge;

import model.map.Hex;
import model.map.Map;
import model.storage.Cost;

public class Road extends EdgeComponent {

    private final static Cost cost = new Cost(1, 2, 0, 0, 1);

    public Road(Hex hex1, Hex hex2, Map map) {
        super(hex1, hex2, map);
    }

    public static Cost getBuildCost() { return cost; }

    @Override
    public String getImageName() { return "/utils/roadmap.png"; }
}
