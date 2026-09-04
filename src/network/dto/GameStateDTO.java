package network.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameStateDTO {

    public int turnNumber;
    public MapDTO map;
    public List<EmpireStateDTO> empires = new ArrayList<>();
    public List<UnitDTO> units = new ArrayList<>();
    public List<BuildingDTO> buildings = new ArrayList<>();
    public List<TribeDTO> tribes = new ArrayList<>();
    public Map<String, String> playerNames = new HashMap<>();

    public GameStateDTO() {}
}
