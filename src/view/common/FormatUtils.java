package view.common;

import model.storage.Cost;

public final class FormatUtils {

    private FormatUtils() {}

    public static String formatEnum(String name) {
        StringBuilder formatted = new StringBuilder();
        boolean nextUpper = true;
        for (char c : name.toCharArray()) {
            if (c == '_') {
                formatted.append(' ');
                nextUpper = true;
            } else if (nextUpper) {
                formatted.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                formatted.append(Character.toLowerCase(c));
            }
        }
        return formatted.toString();
    }

    public static String formatCost(Cost cost, boolean shortForm) {
        if (cost == null) return shortForm ? "" : "Cost: None";
        StringBuilder sb = new StringBuilder();
        if (!shortForm) sb.append("Cost: ");
        boolean first = true;
        if (cost.getWoodNeeded() > 0) {
            if (!first && !shortForm) sb.append(", ");
            else if (!first && shortForm) sb.append(" ");
            sb.append(cost.getWoodNeeded()).append(shortForm ? "W" : " Wood");
            first = false;
        }
        if (cost.getStoneNeeded() > 0) {
            if (!first && !shortForm) sb.append(", ");
            else if (!first && shortForm) sb.append(" ");
            sb.append(cost.getStoneNeeded()).append(shortForm ? "S" : " Stone");
            first = false;
        }
        if (cost.getIronNeeded() > 0) {
            if (!first && !shortForm) sb.append(", ");
            else if (!first && shortForm) sb.append(" ");
            sb.append(cost.getIronNeeded()).append(shortForm ? "I" : " Iron");
            first = false;
        }
        if (cost.getFoodNeeded() > 0) {
            if (!first && !shortForm) sb.append(", ");
            else if (!first && shortForm) sb.append(" ");
            sb.append(cost.getFoodNeeded()).append(shortForm ? "F" : " Food");
        }
        if (first && !shortForm) sb.append("None");
        return sb.toString();
    }
}
