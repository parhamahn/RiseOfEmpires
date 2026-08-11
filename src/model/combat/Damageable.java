package model.combat;

public interface Damageable {

    int getHealth();

    void setHealth(int value);

    default boolean isDead() {
        return getHealth() <= 0;
    }
}
