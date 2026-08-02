package model.building.edge;

import model.map.Hex;
import model.map.Map;

import java.io.Serializable;

public abstract class EdgeComponent implements Serializable {
private static final long serialVersionUID = 1L;

    private final Hex hexA;
    private final Hex hexB;

    public EdgeComponent(Hex hex1, Hex hex2, Map map) {
        Hex a = null;
        Hex b = null;

        if (hex1 != null && hex2 != null && !hex1.equals(hex2)
                && map.getNeighbourHexes(hex1).contains(hex2)) {
            if (hex1.getR() != hex2.getR()) {
                if (hex1.getR() < hex2.getR()) { a = hex1; b = hex2; }
                else { a = hex2; b = hex1; }
            } else {
                if (hex1.getC() < hex2.getC()) { a = hex1; b = hex2; }
                else { a = hex2; b = hex1; }
            }
        }

        this.hexA = a;
        this.hexB = b;
    }

    public boolean isValid() {
        return hexA != null && hexB != null;
    }

    public Hex getHexA() { return hexA; }
    public Hex getHexB() { return hexB; }

    public boolean connects(Hex hex1, Hex hex2) {
        return isValid() && ((hexA == hex1 && hexB == hex2) || (hexA == hex2 && hexB == hex1));
    }

    public abstract String getImageName();
}
