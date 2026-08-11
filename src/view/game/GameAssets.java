package view.game;

import model.MapStructure;
import model.building.edge.EdgeComponent;
import model.building.edge.River;
import model.building.edge.Road;
import model.building.edge.Wall;
import model.building.empire.Bazaar;
import model.building.empire.Dock;
import model.building.empire.Farm;
import model.building.empire.IronMine;
import model.building.empire.LumberMill;
import model.building.empire.MilitaryStable;
import model.building.empire.Monument;
import model.building.empire.Stable;
import model.building.empire.StoneMine;
import model.building.empire.Town;
import model.building.empire.TownHall;
import model.building.other.TradingCenter;
import model.building.tribes.BeachTribe;
import model.building.tribes.FarmerTribe;
import model.building.tribes.MountainTribe;
import model.building.tribes.TraderTribe;
import model.building.tribes.WarriorTribe;
import model.unit.Unit;

public final class GameAssets {

    private GameAssets() {
    }

    public static String buildingImage(MapStructure structure) {
        if (structure instanceof TownHall) return "townhall";
        if (structure instanceof Farm) return "farmhex";
        if (structure instanceof LumberMill) return "lumbermillhex";
        if (structure instanceof StoneMine) return "stoneminehex";
        if (structure instanceof IronMine) return "ironminehex";
        if (structure instanceof Stable) return "stablehex";
        if (structure instanceof Town) return "townhex";
        if (structure instanceof Monument) return "monumenthex";
        if (structure instanceof Bazaar) return "bazaarhex";
        if (structure instanceof Dock) return "dockhex";
        if (structure instanceof TradingCenter) return "tradingcenterhex";
        if (structure instanceof MilitaryStable) return "militarystablehex";
        if (structure instanceof FarmerTribe) return "farmertribe";
        if (structure instanceof MountainTribe) return "mountaintribe";
        if (structure instanceof WarriorTribe) return "warriortribe";
        if (structure instanceof TraderTribe) return "tradertribe";
        if (structure instanceof BeachTribe) return "beachtribe";
        return null;
    }

    public static String unitImage(Unit unit) {
        return unit.getUnitType().toString().toLowerCase().replace("_", "");
    }

    public static String edgeImage(EdgeComponent component) {
        if (component instanceof Wall) return "/utils/wallmap.png";
        if (component instanceof Road) return "/utils/roadmap.png";
        if (component instanceof River) return "/utils/rivermap.png";
        return null;
    }
}
