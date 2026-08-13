package view.buildingpanel.trading;

import model.state.GameState;
import model.storage.ResourceType;
import view.common.BaseModalPanel;

import javax.swing.*;
import java.awt.*;

public abstract class TradingPanel extends BaseModalPanel {

    protected ToGiveSection toGiveSection;
    protected ToGetSection toGetSection;
    protected QuantitySection quantitySection;
    protected DealSection dealSection;

    protected TradingPanel(GameState gameState, String title, String backgroundPath) {
        super(gameState, title, backgroundPath);
    }

    @Override
    protected JPanel buildBody() {
        JPanel body = new JPanel(new GridLayout(2, 2, 15, 15));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        toGiveSection = new ToGiveSection(ResourceType.values());
        toGetSection = new ToGetSection(ResourceType.values());
        quantitySection = new QuantitySection();
        dealSection = new DealSection();

        body.add(toGiveSection);
        body.add(toGetSection);
        body.add(quantitySection);
        body.add(dealSection);

        return body;
    }

    @Override
    public void refresh() {
        toGiveSection.reset();
        toGetSection.reset();
        quantitySection.reset();
        dealSection.setSummary(null);
        dealSection.setDealState(false, "Select resources to make a deal");
    }

    public ToGiveSection getToGiveSection() { return toGiveSection; }
    public ToGetSection getToGetSection() { return toGetSection; }
    public QuantitySection getQuantitySection() { return quantitySection; }
    public DealSection getDealSection() { return dealSection; }

    public int getQuantityValue() {
        return quantitySection.getSelectedQuantity();
    }

    public void wireQuantityOnChange(Runnable r) {
        quantitySection.setOnChange(r);
    }
}
