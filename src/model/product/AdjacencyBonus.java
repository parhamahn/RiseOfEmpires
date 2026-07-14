package model.product;

import model.building.Building;
import model.building.Farm;
import model.building.IronMine;
import model.building.LumberMill;
import model.building.StoneMine;
import model.map.Hex;
import model.map.Map;

import java.util.List;

public class AdjacencyBonus {

    public static class ExtraResources{
        private int extraWood;
        private int extraStone;
        private int extraIron;
        private int extraFood;

        public ExtraResources(int extraWood, int extraStone, int extraIron, int extraFood) {
            this.extraWood = extraWood;
            this.extraStone = extraStone;
            this.extraIron = extraIron;
            this.extraFood = extraFood;
        }

        public int getExtraWood() {
            return extraWood;
        }

        public void setExtraWood(int extraWood) {
            this.extraWood = extraWood;
        }

        public int getExtraStone() {
            return extraStone;
        }

        public void setExtraStone(int extraStone) {
            this.extraStone = extraStone;
        }

        public int getExtraIron() {
            return extraIron;
        }

        public void setExtraIron(int extraIron) {
            this.extraIron = extraIron;
        }

        public int getExtraFood() {
            return extraFood;
        }

        public void setExtraFood(int extraFood) {
            this.extraFood = extraFood;
        }
    }

    private static int checkForFarm(List<Building> buildings, Map map) {
        int adjacentFarmEdges = 0;
        for (Building building : buildings) {
            if (building instanceof Farm) {
                for (Hex neighbor : map.getNeighbourHexes(building.getHex())) {
                    if (neighbor != null && neighbor.getBuilding() instanceof Farm) {
                        adjacentFarmEdges++;
                    }
                }
            }
        }
        return adjacentFarmEdges / 2;
    }

    private static int checkForBeachWood(List<Building> buildings, Map map) {
        int extraWood = 0;
        for (Building building : buildings) {
            if (building instanceof LumberMill) {
                for (Hex hex : map.getNeighbourHexes(building.getHex())) {
                    if (hex != null && hex.getTerrainType() == Hex.TerrainType.SEA) {
                        extraWood += 2;
                        break;
                    }
                }
            }
        }
        return extraWood;
    }

    private static int checkForDeepMineStone(List<Building> buildings, Map map) {
        int extraStone = 0;
        for (Building building : buildings) {
            if (building instanceof StoneMine) {
                int mountainCount = 0;
                for (Hex hex : map.getNeighbourHexes(building.getHex())) {
                    if (hex != null && hex.getTerrainType() == Hex.TerrainType.MOUNTAIN) {
                        mountainCount++;
                    }
                }
                if (mountainCount >= 2) extraStone += 1;
            }
        }
        return extraStone;
    }

    private static int checkForDeepMineIron(List<Building> buildings, Map map) {
        int extraIron = 0;
        for (Building building : buildings) {
            if (building instanceof IronMine) {
                int mountainCount = 0;
                for (Hex hex : map.getNeighbourHexes(building.getHex())) {
                    if (hex != null && hex.getTerrainType() == Hex.TerrainType.MOUNTAIN) {
                        mountainCount++;
                    }
                }
                if (mountainCount >= 2) extraIron += 1;
            }
        }
        return extraIron;
    }

    public static ExtraResources checkForAdjacencyBonus(List<Building> buildings, Map map) {
        int extraFood = checkForFarm(buildings, map);
        int extraWood = checkForBeachWood(buildings, map);
        int extraStone = checkForDeepMineStone(buildings, map);
        int extraIron = checkForDeepMineIron(buildings, map);
        return new ExtraResources(extraWood, extraStone, extraIron, extraFood);
    }
}