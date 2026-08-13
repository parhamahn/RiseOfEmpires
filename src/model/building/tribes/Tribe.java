package model.building.tribes;

import helpers.other.RandomNameGenerator;
import model.MapStructure;
import model.map.Hex;
import model.mission.Mission;
import model.mission.MissionStatus;
import model.state.GameState;
import model.storage.Storage;
import model.trade.tribe.TribeTrade;
import model.unit.Unit;
import model.unit.enemy.Guard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Tribe extends MapStructure implements Serializable {
private static final long serialVersionUID = 1L;

    private int relation;
    private RelationStatus relationStatus;
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
        this.discovered = false;
        this.allianceLockTurns = 0;
        this.lastMissionTurn = 0;
        changeRelation(0, Collections.emptyList());
        activeMission = null;
    }

    public void changeRelation(int newRelation, List<Tribe> allies) {
        newRelation = Math.min(100, Math.max(-100, newRelation));
        this.relation = newRelation;

        if (newRelation >= 70) {
            this.relationStatus = RelationStatus.ALLY;
            isPermanentReward = true;
        } else if (newRelation >= 20) {
            this.relationStatus = RelationStatus.FRIEND;
        } else if (newRelation >= -19) {
            this.relationStatus = RelationStatus.NEUTRAL;
        } else if (newRelation >= -49) {
            this.relationStatus = RelationStatus.UNSATISFIED;
        } else {
            this.relationStatus = RelationStatus.ENEMY;
            isPermanentReward = false;
        }

        if (newRelation < 70 && allies != null && allies.contains(this)) {
            allies.remove(this);
        }
    }

    public boolean isAllianceBlocked(List<Tribe> allies) {
        return false;
    }

    public boolean isCanTrade() {
        return getRelationStatus().canTrade() && !usedThisTurn;
    }

    public boolean isCanGetMission() {
        return getRelationStatus().canGetMission();
    }

    public boolean isCanRequestAlliance(List<Tribe> allies) {
        return getRelationStatus().canRequestAlliance()
                && allianceLockTurns <= 0
                && !isAllianceBlocked(allies);
    }

    public boolean isCanRequestPeace() {
        return getRelationStatus().canRequestPeace();
    }

    public boolean isCanDeclareWar() {
        return getRelationStatus().canDeclareWar();
    }

    public boolean isCanGift() {
        return getRelationStatus().canGift();
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

    public void tickActiveMission(GameState gameState) {
        if (activeMission == null) return;
        activeMission.decreaseDeadline();
        if (activeMission.getDeadline() == 0) {
            activeMission.expire(gameState);
        }
    }

    public void refreshMissions(int currentTurn) {
        if (activeMission == null
                && (getRelationStatus() == RelationStatus.FRIEND || getRelationStatus() == RelationStatus.ALLY)
                && currentTurn - lastMissionTurn >= 5) {
            for (Mission mission : getMissions()) {
                mission.setStatus(MissionStatus.AVAILABLE);
                mission.resetDeadline();
            }
            lastMissionTurn = currentTurn;
        }
    }

    public void destroyCamp(GameState gameState) {
        Hex campHex = getHex();
        gameState.getWorldState().getTribes().remove(this);
        gameState.getEmpireState().getAllies().remove(this);
        if (activeMission != null) {
            activeMission.setStatus(MissionStatus.FAILED);
            activeMission = null;
        }
        List<Unit> guards = new ArrayList<>(campHex.getUnits());
        for (Unit unit : guards) {
            if (unit instanceof Guard) {
                campHex.getUnits().remove(unit);
                gameState.getWorldState().getEnemies().remove(unit);
            }
        }
        campHex.setBuilding(null);
        campHex.setInEmpireBorder(true);
        for (Hex neighbour : gameState.getWorldState().getMap().getNeighbourHexes(campHex)) {
            if (neighbour != null
                    && neighbour.getTerrainType() != Hex.TerrainType.SEA
                    && neighbour.getTerrainType() != Hex.TerrainType.TALL_MOUNTAIN
                    && !neighbour.isInEmpireBorder()) {
                neighbour.setInEmpireBorder(true);
            }
        }
        collectLoot(gameState.getEmpireState().getStorage());
    }

    protected abstract void collectLoot(Storage storage);
}
