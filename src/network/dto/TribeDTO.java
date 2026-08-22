package network.dto;

public class TribeDTO {

    public String id;
    public String type;
    public int hexC;
    public int hexR;
    public String name;
    public int hp;
    public int maxHp;
    public int relation;
    public String relationStatus;
    public boolean discovered;
    public boolean permanentReward;
    public boolean usedThisTurn;
    public int allianceLockTurns;
    public int lastMissionTurn;
    public MissionDTO activeMission;
    public double tradeRate;

    public TribeDTO() {}
}
