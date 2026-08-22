package network.dto.mapper;

import model.map.Hex;
import network.dto.DtoMapper;
import network.dto.HexDTO;

import java.util.HashMap;

public enum HexMapper implements DtoMapper<Hex, HexDTO> {
    INSTANCE;

    @Override
    public HexDTO toDTO(Hex hex) {
        HexDTO dto = new HexDTO();
        dto.c = hex.getC();
        dto.r = hex.getR();
        dto.terrainType = hex.getTerrainType().name();
        dto.discovered = hex.isDiscovered();
        dto.inEmpireBorder = hex.isInEmpireBorder();
        dto.resources = new HashMap<>();
        for (HashMap.Entry<Hex.ResourceType, Integer> entry : hex.getResources().entrySet()) {
            dto.resources.put(entry.getKey().name(), entry.getValue());
        }
        return dto;
    }
}
