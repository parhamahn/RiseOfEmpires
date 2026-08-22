package network.dto;

import java.util.List;

public class MapDTO {

    public int rows;
    public int cols;
    public List<HexDTO> hexes;
    public List<EdgeDTO> edges;

    public MapDTO() {}
}
