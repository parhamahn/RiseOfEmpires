package network.dto;

import java.util.ArrayList;
import java.util.List;

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
    public List<String> discoveredByPlayerIds = new ArrayList<>();
    public boolean permanentReward;
    public boolean usedThisTurn;
    public int allianceLockTurns;
    public int lastMissionTurn;
    public MissionDTO activeMission;
    public double tradeRate;

    public TribeDTO() {}
}
