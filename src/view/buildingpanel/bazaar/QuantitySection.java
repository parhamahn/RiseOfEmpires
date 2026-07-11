package view.buildingpanel.bazaar;

import java.awt.*;

public class QuantitySection extends OptionSection {

    private static final String[] TEXTS = {"10", "100", "500"};
    private static final int[] QUANTITIES = {10, 100, 500};

    public QuantitySection() {
        super("Quantity :", TEXTS, new Dimension(100, 100));
    }

    public int getSelectedQuantity() {
        int i = getSelectedIndex();
        return i >= 0 ? QUANTITIES[i] : 0;
    }
}
