package model.item;

import model.map.Hex;
import model.unit.Unit;

public class TeleportItem extends Item{

    private Hex fromHex;
    private Hex toHex;

    public TeleportItem(Unit unit, Hex targetLocation){
        super(unit);
        fromHex = unit.getHex();
        toHex = targetLocation;
    }

    @Override
    public void effect(){
        if(isAvailable()==null){
            toHex.getUnits().add(super.getUnit());
            super.getUnit().setHex(toHex);
        }
    }

    @Override
    public String isAvailable(){
        if(fromHex==toHex){
            return "Can't teleport to same hex";
        }
        else if(super.getUnit().isUsedItem()){
            return "Unit already used an item";
        }
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
