package network.dto;

import java.util.HashMap;

public class HexDTO {

    public int c;
    public int r;
    public String terrainType;
    public boolean discovered;
    public boolean inEmpireBorder;
    public HashMap<String, Integer> resources;

    public HexDTO(){}

}
