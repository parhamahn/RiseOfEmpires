package model;

import model.map.Hex;

import java.io.Serializable;

public abstract class MapStructure extends Entity implements Serializable {
private static final long serialVersionUID = 1L;

    private Hex hex;
    private int HP;
    private int maxHP;

    protected MapStructure(Hex hex, int maxHP) {
        super();
        this.hex = hex;
        this.maxHP = maxHP;
        this.HP = maxHP;
    }

    public Hex getHex() {
        return hex;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public boolean isTownHall() {
        return false;
    }
}
