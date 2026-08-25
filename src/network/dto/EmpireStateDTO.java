package network.dto;

import java.util.ArrayList;
import java.util.List;

public class EmpireStateDTO {

    public String playerId;
    public StorageDTO storage;
    public ProductionDTO production;
    public int maxUnit;
    public boolean starving;
    public boolean upkeepDisaster;
    public List<int[]> townHallHexes = new ArrayList<>();
    public List<QueueDTO> queues = new ArrayList<>();
    public List<String> researchedTechnologies = new ArrayList<>();
    public String townHallLevel;
    public int happiness;
    public boolean dockDiscount;
    public List<String> allyTribeIds = new ArrayList<>();

    public EmpireStateDTO() {}
}
