package model.building.edge;

import model.map.Hex;
import model.map.Map;

import java.io.Serializable;

public class River extends EdgeComponent implements Serializable {
private static final long serialVersionUID = 1L;

    public River(Hex hex1, Hex hex2, Map map) {
        super(hex1, hex2, map);
    }
}
