package network.dto.mapper;

import model.product.Production;
import network.dto.DtoMapper;
import network.dto.ProductionDTO;

public enum ProductionMapper implements DtoMapper<Production, ProductionDTO> {
    INSTANCE;

    @Override
    public ProductionDTO toDTO(Production production) {
        ProductionDTO dto = new ProductionDTO();
        dto.wood = production.getWoodProduction();
        dto.stone = production.getStoneProduction();
        dto.iron = production.getIronProduction();
        dto.food = production.getFoodProduction();
        return dto;
    }
}
