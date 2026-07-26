package view.buildingpanel.trading;

import model.state.GameState;
import model.storage.ResourceType;
import view.buildingpanel.tribe.SliderSection;

import javax.swing.*;
import java.awt.*;

public class TradingCenterPanel extends TradingPanel {

    private SliderSection sliderSection;

    public TradingCenterPanel(GameState gameState) {
        super(gameState, "Trading Center", "/utils/tradingcenterpanel.png");
    }

    @Override
    protected JPanel buildBody() {
        JPanel body = new JPanel(new GridLayout(2, 2, 15, 15));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        toGiveSection = new ToGiveSection(ResourceType.values());
        toGetSection = new ToGetSection(ResourceType.values());
        sliderSection = new SliderSection("Quantity :");
        dealSection = new DealSection();

        body.add(toGiveSection);
        body.add(toGetSection);
        body.add(sliderSection);
        body.add(dealSection);

        return body;
    }

    @Override
    public void refresh() {
        toGiveSection.reset();
        toGetSection.reset();
        sliderSection.reset();
        sliderSection.setMax(0);
        dealSection.setSummary(null);
        dealSection.getDealButton().setEnabled(false);
    }

    @Override
    public int getQuantityValue() {
        return sliderSection.getValue();
    }

    @Override
    public void wireQuantityOnChange(Runnable r) {
        sliderSection.setOnChange(r);
    }

    public SliderSection getSliderSection() {
        return sliderSection;
    }
}
