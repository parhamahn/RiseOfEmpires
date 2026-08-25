package model.engine;

import model.map.Hex;

import java.util.List;

public class GameEvent {

    public enum Type {
        STARVATION_STARTED,
        STARVATION_ENDED,
        MISSION_FAILED,
        NEW_MISSION_AVAILABLE,
        GUARD_SPAWNED,
        TRIBE_NOW_ENEMY,
        SEASON_CHANGED,
        QUEUE_COMPLETED,
        TRIBE_ATTACKED_PLAYER
    }

    private final Type type;
    private final String detail;
    private final List<Hex> hexes;

    public GameEvent(Type type) {
        this(type, null);
    }

    public GameEvent(Type type, String detail) {
        this(type, detail, List.of());
    }

    public GameEvent(Type type, String detail, List<Hex> hexes) {
        this.type = type;
        this.detail = detail;
        this.hexes = hexes == null ? List.of() : hexes;
    }

    public Type getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public List<Hex> getHexes() {
        return hexes;
    }
}
