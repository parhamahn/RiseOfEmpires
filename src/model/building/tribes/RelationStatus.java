package model.building.tribes;

public enum RelationStatus implements RelationState {
    ALLY(true, true, true, false, true, true),
    FRIEND(true, true, false, false, true, true),
    NEUTRAL(false, false, false, false, true, true),
    UNSATISFIED(false, false, false, false, true, true),
    ENEMY(false, false, false, true, false, false);

    private final boolean canTrade;
    private final boolean canGetMission;
    private final boolean canRequestAlliance;
    private final boolean canRequestPeace;
    private final boolean canDeclareWar;
    private final boolean canGift;

    RelationStatus(boolean canTrade, boolean canGetMission, boolean canRequestAlliance,
                   boolean canRequestPeace, boolean canDeclareWar, boolean canGift) {
        this.canTrade = canTrade;
        this.canGetMission = canGetMission;
        this.canRequestAlliance = canRequestAlliance;
        this.canRequestPeace = canRequestPeace;
        this.canDeclareWar = canDeclareWar;
        this.canGift = canGift;
    }

    @Override
    public boolean canTrade() {
        return canTrade;
    }

    @Override
    public boolean canGetMission() {
        return canGetMission;
    }

    @Override
    public boolean canRequestAlliance() {
        return canRequestAlliance;
    }

    @Override
    public boolean canRequestPeace() {
        return canRequestPeace;
    }

    @Override
    public boolean canDeclareWar() {
        return canDeclareWar;
    }

    @Override
    public boolean canGift() {
        return canGift;
    }
}
