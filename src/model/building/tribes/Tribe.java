package model.building.tribes;

import helpers.other.RandomNameGenerator;
import model.MapStructure;
import model.map.Hex;
import model.mission.Mission;
import model.trade.tribe.TribeTrade;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public abstract class Tribe extends MapStructure implements Serializable {
private static final long serialVersionUID = 1L;

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
    private Mission activeMission;
    private boolean isPermanentReward;
    private boolean usedThisTurn;
    private boolean discovered;
    private int allianceLockTurns;
    private int lastMissionTurn;

    public Tribe(Hex hex, int maxHP) {
        super(hex, maxHP);
        this.name = RandomNameGenerator.generateRandomName();
        this.isPermanentReward = false;
        this.discovered = true;
        this.allianceLockTurns = 0;
        this.lastMissionTurn = 0;
        changeRelation(0, Collections.emptyList());
        activeMission = null;
    }

    public void changeRelation(int newRelation, List<Tribe> allies) {
        newRelation = Math.min(100, newRelation);
        newRelation = Math.max(-100, newRelation);
        this.relation = newRelation;

        if(newRelation>=70 && newRelation<=100) {
            this.relationStatus = RelationStatus.ALLY;
            setOptions(true, true, true,false, true, true);
            isPermanentReward = true;

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
            isPermanentReward = false;
        }

        if (newRelation < 70 && allies != null && allies.contains(this)) {
            allies.remove(this);
        }


        if(this instanceof FarmerTribe){
            for(Tribe tribe:allies){
                if(tribe instanceof MountainTribe || tribe instanceof WarriorTribe){
                    canRequestAlliance = false;
                    break;
                }
            }
        }else if(this instanceof WarriorTribe){
            if(!allies.isEmpty()){
                canRequestAlliance = false;
            }
        }else if(this instanceof TraderTribe){
            for(Tribe tribe:allies){
                if(tribe instanceof WarriorTribe){
                    canRequestAlliance = false;
                    break;
                }
            }
        }else if(this instanceof MountainTribe){
            for(Tribe tribe:allies){
                if(tribe instanceof FarmerTribe || tribe instanceof WarriorTribe){
                    canRequestAlliance = false;
                    break;
                }
            }
        }else if(this instanceof BeachTribe){
            for(Tribe tribe:allies){
                if(tribe instanceof WarriorTribe){
                    canRequestAlliance = false;
                    break;
                }
            }
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
        return canTrade && !usedThisTurn;
    }

    public boolean isCanGetMission() {
        return canGetMission;
    }

    public boolean isCanRequestAlliance() {
        return canRequestAlliance && allianceLockTurns <= 0;
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

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public void setRelationStatus(RelationStatus relationStatus) {
        this.relationStatus = relationStatus;
    }

    public void setCanTrade(boolean canTrade) {
        this.canTrade = canTrade;
    }

    public void setCanGetMission(boolean canGetMission) {
        this.canGetMission = canGetMission;
    }

    public void setCanRequestAlliance(boolean canRequestAlliance) {
        this.canRequestAlliance = canRequestAlliance;
    }

    public void setCanRequestPeace(boolean canRequestPeace) {
        this.canRequestPeace = canRequestPeace;
    }

    public void setCanDeclareWar(boolean canDeclareWar) {
        this.canDeclareWar = canDeclareWar;
    }

    public void setCanGift(boolean canGift) {
        this.canGift = canGift;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelation() {
        return relation;
    }

    public String getName() {
        return name;
    }

    public abstract List<Mission> getMissions();

    public TribeTrade getTradeStrategy() {
        return null;
    }

    public Mission getActiveMission() {
        return activeMission;
    }

    public void setActiveMission(Mission activeMission) {
        this.activeMission = activeMission;
    }

    public boolean isPermanentReward() {
        return isPermanentReward;
    }

    public void setPermanentReward(boolean permanentReward) {
        isPermanentReward = permanentReward;
    }

    public boolean isUsedThisTurn() {
        return usedThisTurn;
    }

    public void setUsedThisTurn(boolean usedThisTurn) {
        this.usedThisTurn = usedThisTurn;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public int getAllianceLockTurns() {
        return allianceLockTurns;
    }

    public void setAllianceLockTurns(int allianceLockTurns) {
        this.allianceLockTurns = allianceLockTurns;
    }

    public int getLastMissionTurn() {
        return lastMissionTurn;
    }

    public void setLastMissionTurn(int lastMissionTurn) {
        this.lastMissionTurn = lastMissionTurn;
    }
}
