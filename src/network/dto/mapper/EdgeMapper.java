package network.dto.mapper;

import model.building.edge.EdgeComponent;
import model.building.edge.River;
import model.building.edge.Road;
import model.building.edge.Wall;
import network.dto.DtoMapper;
import network.dto.EdgeDTO;

public enum EdgeMapper implements DtoMapper<EdgeComponent, EdgeDTO> {
    INSTANCE;

    @Override
    public EdgeDTO toDTO(EdgeComponent edge) {
        EdgeDTO dto = new EdgeDTO();
        dto.hexAC = edge.getHexA().getC();
        dto.hexAR = edge.getHexA().getR();
        dto.hexBC = edge.getHexB().getC();
        dto.hexBR = edge.getHexB().getR();
        if (edge instanceof Wall wall) {
            dto.kind = "WALL";
            dto.hp = wall.getHP();
        } else if (edge instanceof Road) {
            dto.kind = "ROAD";
        } else if (edge instanceof River) {
            dto.kind = "RIVER";
        }
        return dto;
    }
}
