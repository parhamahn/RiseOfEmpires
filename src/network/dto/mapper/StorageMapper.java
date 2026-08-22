package network.dto.mapper;

import model.storage.Storage;
import network.dto.DtoMapper;
import network.dto.StorageDTO;

public enum StorageMapper implements DtoMapper<Storage, StorageDTO> {
    INSTANCE;

    @Override
    public StorageDTO toDTO(Storage storage) {
        StorageDTO dto = new StorageDTO();
        dto.wood = storage.getWoodQuantity();
        dto.stone = storage.getStoneQuantity();
        dto.iron = storage.getIronQuantity();
        dto.food = storage.getFoodQuantity();
        dto.woodMax = storage.getWoodMax();
        dto.stoneMax = storage.getStoneMax();
        dto.ironMax = storage.getIronMax();
        dto.foodMax = storage.getFoodMax();
        return dto;
    }
}
