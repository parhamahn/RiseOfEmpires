package model.building.edge;

import model.map.Hex;
import model.map.Map;
import model.storage.Cost;

public class Wall extends EdgeComponent {

    private final int MaxHP = 30;
    private int HP;

    private final static Cost cost = new Cost(0, 5, 0, 0, 2);

    public Wall(Hex hex1, Hex hex2, Map map) {
        super(hex1, hex2, map);
        HP = MaxHP;
    }

    public int getMaxHP() {
        return MaxHP;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public static Cost getBuildCost() { return cost; }

    @Override
    public String getImageName() { return "/utils/wallmap.png"; }
}
