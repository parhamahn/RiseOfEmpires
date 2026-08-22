package network.dto;

import java.util.ArrayList;
import java.util.List;

public class GameStateDTO {

    public int turnNumber;
    public MapDTO map;
    public List<EmpireStateDTO> empires = new ArrayList<>();
    public List<UnitDTO> units = new ArrayList<>();
    public List<BuildingDTO> buildings = new ArrayList<>();
    public List<TribeDTO> tribes = new ArrayList<>();

    public GameStateDTO() {}
}
