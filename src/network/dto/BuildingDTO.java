package network.dto;

import java.util.ArrayList;
import java.util.List;

public class BuildingDTO {

    public String id;
    public String type;
    public String ownerId;
    public int hexC;
    public int hexR;
    public int hp;
    public int maxHp;
    public boolean producingStopped;
    public int noUpkeepTurns;
    public List<String> assignedWorkerIds = new ArrayList<>();
    public String resourceType;
    public Boolean usedThisTurn;

    public BuildingDTO() {}
}
