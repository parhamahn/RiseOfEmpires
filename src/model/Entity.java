package model;

import java.io.Serializable;
import java.util.UUID;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final long createdAt;

    protected Entity() {
        this(UUID.randomUUID());
    }

    protected Entity(UUID id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() { return id; }
    public long getCreatedAt() { return createdAt; }
}