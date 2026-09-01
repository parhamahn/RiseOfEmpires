package network.dto.mapper;

import model.building.edge.River;
import model.building.edge.Road;
import model.building.edge.Wall;
import model.map.Hex;
import model.map.Map;
import model.building.edge.EdgeComponent;
import network.dto.DtoMapper;
import network.dto.EdgeDTO;
import network.dto.HexDTO;
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

    public Map fromDTO(MapDTO dto, java.util.UUID singleEmpireOwner) {
        Map map = new Map(dto.rows, dto.cols);
        Hex[][] lookup = new Hex[dto.rows][dto.cols];
        for (HexDTO hexDTO : dto.hexes) {
            Hex hex = HexMapper.INSTANCE.fromDTO(hexDTO);
            if (hexDTO.inEmpireBorder && singleEmpireOwner != null) {
                hex.setBorderOwner(singleEmpireOwner);
            }
            map.setHex(hexDTO.r, hexDTO.c, hex);
            lookup[hexDTO.r][hexDTO.c] = hex;
        }
        for (EdgeDTO edgeDTO : dto.edges) {
            Hex a = lookup[edgeDTO.hexAR][edgeDTO.hexAC];
            Hex b = lookup[edgeDTO.hexBR][edgeDTO.hexBC];
            if (a == null || b == null) continue;
            switch (edgeDTO.kind) {
                case "WALL" -> {
                    Wall wall = new Wall(a, b, map);
                    if (wall.isValid()) {
                        wall.setHP(edgeDTO.hp);
                        a.getWallsWith().add(wall);
                        b.getWallsWith().add(wall);
                    }
                }
                case "ROAD" -> {
                    Road road = new Road(a, b, map);
                    if (road.isValid()) {
                        a.getRoadsTo().add(road);
                        b.getRoadsTo().add(road);
                    }
                }
                case "RIVER" -> {
                    River river = new River(a, b, map);
                    if (river.isValid()) {
                        a.getRiversWith().add(river);
                        b.getRiversWith().add(river);
                    }
                }
            }
        }
        return map;
    }

}
