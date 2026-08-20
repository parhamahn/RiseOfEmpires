package model;

import java.util.UUID;

public class Player extends Entity {
    private String name;
    private boolean ready;

    public Player(UUID id, String name) {
        super(id);
        this.name = name;
        this.ready = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}