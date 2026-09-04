package network.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HexDTO {

    public int c;
    public int r;
    public String terrainType;
    public List<String> discoveredByPlayerIds = new ArrayList<>();
    public boolean inEmpireBorder;
    public HashMap<String, Integer> resources;

    public HexDTO(){}

}
