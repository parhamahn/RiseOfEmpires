package network.dto.mapper;

import model.storage.MaxStorage;
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
        dto.actionItemCount = storage.getActionItemCount();
        dto.teleportItemCount = storage.getTeleportItemCount();
        dto.combatItemCount = storage.getCombatItemCount();
        return dto;
    }

    public Storage fromDTO(StorageDTO dto) {
        Storage storage = new Storage();
        storage.setWoodQuantity(dto.wood);
        storage.setStoneQuantity(dto.stone);
        storage.setIronQuantity(dto.iron);
        storage.setFoodQuantity(dto.food);
        storage.upgradeStorage(new MaxStorage(dto.foodMax, dto.woodMax, dto.stoneMax, dto.ironMax));
        for (int i = 0; i < dto.teleportItemCount; i++) storage.produceTeleportItem();
        for (int i = 0; i < dto.actionItemCount; i++) storage.produceActionItem();
        for (int i = 0; i < dto.combatItemCount; i++) storage.produceCombatItem();
        return storage;
    }

}
