package model.building.tribes;

import helpers.RandomNameGenerator;
import model.building.Building;
import model.map.Hex;

public abstract class Tribe extends Building {

    public enum RelationStatus {ENEMY, UNSATISFIED, NEUTRAL, FRIEND, ALLY}

    private int relation;
    private RelationStatus relationStatus;
    private boolean canTrade;
    private boolean canGetMission;
    private boolean canRequestAlliance;
    private boolean canRequestPeace;
    private boolean canDeclareWar;
    private boolean canGift;
    private String name;

    private final String imageName;

    public Tribe(Hex hex, String imageName) {
        super(hex, null, null, 0, 200);
        this.imageName = imageName;
        this.name = RandomNameGenerator.generateRandomName();
        changeRelation(0);
    }

    @Override
    public int getProductionPerWorker() {
        return 0;
    }

    @Override
    public String getBuildingImageName() {
        return imageName;
    }

    public void changeRelation(int newRelation) {
        newRelation = Math.min(100, newRelation);
        newRelation = Math.max(-100, newRelation);
        this.relation = newRelation;

        if(newRelation>=70 && newRelation<=100) {
            this.relationStatus = RelationStatus.ALLY;
            setOptions(true, true, true,false, true, true);

        }else if(newRelation>=20 && newRelation<=69) {
            this.relationStatus = RelationStatus.FRIEND;
            setOptions(true, true, false,false, true, true);

        }else if(newRelation>=-19 && newRelation<=19) {
            this.relationStatus = RelationStatus.NEUTRAL;
            setOptions(false, false, false,false, true, true);

        }else if(newRelation>=-49 && newRelation<=-20) {
            this.relationStatus = RelationStatus.UNSATISFIED;
            setOptions(false, false, false,false, true, true);

        }else if(newRelation>=-100 && newRelation<=-50) {
            this.relationStatus = RelationStatus.ENEMY;
            setOptions(false, false, false, true, false, false);
        }

    }

    private void setOptions(boolean canTrade,  boolean canGetMission, boolean canRequestAlliance, boolean canRequestPeace, boolean canDeclareWar, boolean canGift) {
        this.canTrade = canTrade;
        this.canGetMission = canGetMission;
        this.canRequestAlliance = canRequestAlliance;
        this.canRequestPeace = canRequestPeace;
        this.canDeclareWar = canDeclareWar;
        this.canGift = canGift;
    }


    public boolean isCanTrade() {
        return canTrade;
    }

    public boolean isCanGetMission() {
        return canGetMission;
    }

    public boolean isCanRequestAlliance() {
        return canRequestAlliance;
    }

    public boolean isCanRequestPeace() {
        return canRequestPeace;
    }

    public boolean isCanDeclareWar() {
        return canDeclareWar;
    }

    public boolean isCanGift() {
        return canGift;
    }

    public RelationStatus getRelationStatus() {
        return relationStatus;
    }

    public int getRelation() {
        return relation;
    }

    public String getName() {
        return name;
    }
}
