package model.building.tribes;

public interface RelationState {
    boolean canTrade();

    boolean canGetMission();

    boolean canRequestAlliance();

    boolean canRequestPeace();

    boolean canDeclareWar();

    boolean canGift();
}
