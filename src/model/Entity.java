package model;

import java.io.Serializable;
import java.util.UUID;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final long createdAt;
    private UUID ownerID;

    protected Entity() {
        this(UUID.randomUUID());
        this.ownerID = null;
    }

    protected Entity(UUID id) {
        this.id = id;
        this.ownerID = null;
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() { return id; }
    public long getCreatedAt() { return createdAt; }
    public UUID getOwnerID() {return ownerID;}

    public void setOwnerID(UUID ownerID) {
        this.ownerID = ownerID;
    }
}