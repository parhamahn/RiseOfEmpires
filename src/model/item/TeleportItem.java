package model.item;

import model.map.Hex;
import model.storage.Storage;
import model.unit.Unit;

public class TeleportItem extends Item{

    private Hex fromHex;
    private Hex toHex;

    public TeleportItem(Unit unit, Hex targetLocation){
        super(unit);
        fromHex = unit.getHex();
        toHex = targetLocation;
    }

    public void setToHex(Hex toHex) {
        this.toHex = toHex;
    }

    @Override
    public void effect(Storage storage){
        if(isAvailable(storage)==null){
            toHex.getUnits().add(super.getUnit());
            super.getUnit().setHex(toHex);
            fromHex.getUnits().remove(super.getUnit());
            getUnit().setAssigned(false);
            getUnit().setUsedItem(true);
            storage.consumeTeleportItem();
        }
    }

    @Override
    public String isAvailable(Storage storage){
        if(fromHex==toHex){
            return "Can't teleport to same hex";
        }else if(storage.getTeleportItemCount()==0){
            return "You don't have any teleport item";
        }
        else if(super.getUnit().isUsedItem()){
            return "Unit already used an item";
        }

        if (toHex == null) return null;

        else if(!toHex.getUnits().isEmpty()){
            return "Can't teleport to a hex with units";
        }
        else if(!toHex.isDiscovered()){
            return "Can't teleport to a not-discovered hex";
        }
        else if(toHex.getTerrainType()== Hex.TerrainType.SEA || toHex.getTerrainType()== Hex.TerrainType.TALL_MOUNTAIN){
            return "Can't teleport to a Sea or Tall Mountain hex";
        }else{
            return null;
        }
    }

}
