package model.building.edge;

import model.map.Hex;
import model.map.Map;

public class River extends EdgeComponent {

    public River(Hex hex1, Hex hex2, Map map) {
        super(hex1, hex2, map);
    }

    @Override
    public String getImageName() { return "/utils/rivermap.png"; }
}
