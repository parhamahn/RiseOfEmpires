package network.dto.mapper;

import model.map.Hex;
import model.map.Map;
import model.building.edge.EdgeComponent;
import network.dto.DtoMapper;
import network.dto.MapDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum MapMapper implements DtoMapper<Map, MapDTO> {
    INSTANCE;

    @Override
    public MapDTO toDTO(Map map) {
        MapDTO dto = new MapDTO();
        dto.rows = map.getRows();
        dto.cols = map.getCols();

        List<HexDTO> hexes = new ArrayList<>();
        Set<EdgeComponent> edges = new HashSet<>();
        for (Hex hex : map.getAllHexes()) {
            hexes.add(HexMapper.INSTANCE.toDTO(hex));
            edges.addAll(hex.getWallsWith());
            edges.addAll(hex.getRoadsTo());
            edges.addAll(hex.getRiversWith());
        }
        dto.hexes = hexes;

        List<EdgeDTO> edgeDTOs = new ArrayList<>();
        for (EdgeComponent edge : edges) {
            edgeDTOs.add(EdgeMapper.INSTANCE.toDTO(edge));
        }
        dto.edges = edgeDTOs;
        return dto;
    }
}
