package view.buildingpanel.tribe;

import helpers.image.ImageLoader;
import model.building.tribes.Tribe;
import model.storage.ResourceType;
import model.storage.Storage;
import model.state.GameState;
import model.trade.tribe.TribeTrade;
import view.buildingpanel.trading.ToGetSection;
import view.buildingpanel.trading.ToGiveSection;
import view.common.BaseModalPanel;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class TribeTradePanel extends BaseModalPanel {

    private ToGiveSection toGiveSection;
    private ToGetSection toGetSection;
    private SliderSection quantitySection;
    private JLabel summaryLabel;
    private JButton dealButton;
    private JPanel body;

    public TribeTradePanel(GameState gameState) {
        super(gameState, "Trade", "/utils/bazaarpanel.png");
    }

    @Override
    protected JPanel buildBody() {
        toGiveSection = new ToGiveSection(ResourceType.values());
        toGetSection = new ToGetSection(ResourceType.values());
        quantitySection = new SliderSection("Quantity :");

        summaryLabel = new JLabel(" ");
        summaryLabel.setFont(UITheme.GAME_FONT.deriveFont(18f));
        summaryLabel.setForeground(Color.WHITE);
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Icon dealIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 220, 56);
        dealButton = StyledButtons.textButton("Deal", dealIcon, new Dimension(220, 56), 20f, UITheme.MENU_BUTTON_FG);
        dealButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        StyledButtons.setDisabled(dealButton, true, "Select a resource to give and receive and a quantity");

        body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 150, 15, 150));

        body.add(toGiveSection);
        body.add(Box.createRigidArea(new Dimension(0, 20)));
        body.add(toGetSection);
        body.add(Box.createRigidArea(new Dimension(0, 20)));
        body.add(quantitySection);
        body.add(Box.createRigidArea(new Dimension(0, 20)));
        body.add(summaryLabel);
        body.add(Box.createRigidArea(new Dimension(0, 15)));
        body.add(dealButton);

        return body;
    }

    public void setProvidedTypes(ResourceType[] types) {
        body.remove(toGetSection);
        toGetSection = new ToGetSection(types);
        body.add(toGetSection, 2);
        body.revalidate();
        body.repaint();
    }

    @Override
    public void refresh() {
        toGiveSection.reset();
        toGetSection.reset();
        quantitySection.reset();
        quantitySection.setMax(0);
        summaryLabel.setText(" ");
        StyledButtons.setDisabled(dealButton, true, "Select a resource to give and receive and a quantity");
    }

    public void updateSliderMax(Storage storage) {
        ResourceType giveResource = getSelectedGiveResource();
        if (giveResource == null) {
            quantitySection.setMax(0);
        } else {
            quantitySection.setMax(storage.getQuantity(giveResource));
        }
        updateSummary(storage, null);
    }

    public void updateSummary(Storage storage, Tribe tribe) {
        ResourceType giveResource = getSelectedGiveResource();
        ResourceType getResource = getSelectedGetResource();
        int qty = quantitySection.getValue();
        if (giveResource == null || getResource == null || qty <= 0 || tribe == null) {
            summaryLabel.setText(" ");
            StyledButtons.setDisabled(dealButton, true, "Select a resource to give and receive and a quantity");
            return;
        }
        if (storage.getQuantity(giveResource) < qty) {
            summaryLabel.setText("Not enough " + giveResource.name());
            summaryLabel.setForeground(UITheme.NEGATIVE_CHANGE);
            StyledButtons.setDisabled(dealButton, true, "Not enough " + giveResource.name());
            return;
        }

        if(giveResource.equals(getResource)) {
            summaryLabel.setText("You can't get what you give ! ");
            summaryLabel.setForeground(UITheme.NEGATIVE_CHANGE);
            StyledButtons.setDisabled(dealButton, true, "You can't trade a resource for itself");
            return;
        }

        TribeTrade trade = tribe.getTradeStrategy();
        if (trade == null) return;
        int received = (int) Math.floor(qty * trade.getRate());

        if (storage.getQuantity(getResource) + received > storage.getMax(getResource)) {
            summaryLabel.setText("Not enough storage for : " + getResource.name());
            summaryLabel.setForeground(UITheme.NEGATIVE_CHANGE);
            StyledButtons.setDisabled(dealButton, true, "Not enough storage for " + getResource.name());
            return;
        };

        summaryLabel.setText("Give " + qty + " " + giveResource.name() + "  ->  Get " + received + " " + getResource.name());
        summaryLabel.setForeground(UITheme.POSITIVE_CHANGE);
        StyledButtons.setDisabled(dealButton, false, null);
    }

    public ResourceType getSelectedGiveResource() {
        return toGiveSection.getSelectedResource();
    }

    public ResourceType getSelectedGetResource() {
        return toGetSection.getSelectedResource();
    }

    public int getQuantity() {
        return quantitySection.getValue();
    }

    public ToGiveSection getToGiveSection() {
        return toGiveSection;
    }

    public ToGetSection getToGetSection() {
        return toGetSection;
    }

    public SliderSection getQuantitySection() {
        return quantitySection;
    }

    public JButton getDealButton() {
        return dealButton;
    }
}
