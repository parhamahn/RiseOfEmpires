package model.state;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Diplomacy implements Serializable {
private static final long serialVersionUID = 1L;

    public enum PlayerRelation { NEUTRAL, ENEMY, ALLY }

    private final Map<UUID, Map<UUID, PlayerRelation>> relations = new ConcurrentHashMap<>();

    public PlayerRelation getRelation(UUID a, UUID b) {
        if (a == null || b == null || a.equals(b)) return PlayerRelation.NEUTRAL;
        Map<UUID, PlayerRelation> row = relations.get(a);
        return row != null ? row.getOrDefault(b, PlayerRelation.NEUTRAL) : PlayerRelation.NEUTRAL;
    }

    public void setRelation(UUID a, UUID b, PlayerRelation relation) {
        if (a == null || b == null || a.equals(b)) return;
        relations.computeIfAbsent(a, k -> new ConcurrentHashMap<>()).put(b, relation);
        relations.computeIfAbsent(b, k -> new ConcurrentHashMap<>()).put(a, relation);
    }
}
