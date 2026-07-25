package view.buildingpanel.tribe;

import helpers.ImageLoader;
import model.storage.ResourceType;
import model.storage.Storage;
import model.state.GameState;
import view.buildingpanel.trading.OptionSection;
import view.common.BaseModalPanel;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class GiftPanel extends BaseModalPanel {

    private static final String[] ICONS = {"/utils/wood.png", "/utils/stone.png", "/utils/iron.png", "/utils/food.png"};
    private static final ResourceType[] TYPES = {ResourceType.WOOD, ResourceType.STONE, ResourceType.IRON, ResourceType.FOOD};

    private OptionSection resourceSection;
    private SliderSection quantitySection;
    private JLabel summaryLabel;
    private JButton sendButton;

    public GiftPanel(GameState gameState) {
        super(gameState, "Send Gift", "/utils/bazaarpanel.png");
    }

    @Override
    protected JPanel buildBody() {
        resourceSection = new OptionSection("Select Resource :", ICONS, 90, 64);
        quantitySection = new SliderSection("Quantity :");

        summaryLabel = new JLabel(" ");
        summaryLabel.setFont(UITheme.GAME_FONT.deriveFont(18f));
        summaryLabel.setForeground(Color.WHITE);
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Icon sendIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 220, 56);
        sendButton = StyledButtons.textButton("Send Gift", sendIcon, new Dimension(220, 56), 20f, UITheme.MENU_BUTTON_FG);
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendButton.setEnabled(false);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(20, 150, 15, 150));

        body.add(resourceSection);
        body.add(Box.createRigidArea(new Dimension(0, 20)));
        body.add(quantitySection);
        body.add(Box.createRigidArea(new Dimension(0, 20)));
        body.add(summaryLabel);
        body.add(Box.createRigidArea(new Dimension(0, 15)));
        body.add(sendButton);

        return body;
    }

    @Override
    public void refresh() {
        resourceSection.reset();
        quantitySection.reset();
        quantitySection.setMax(0);
        summaryLabel.setText(" ");
        sendButton.setEnabled(false);
    }

    public void updateSliderMax(Storage storage) {
        ResourceType selected = getSelectedResource();
        if (selected == null) {
            quantitySection.setMax(0);
        } else {
            quantitySection.setMax(storage.getQuantity(selected));
        }
        updateSummary(storage);
    }

    public void updateSummary(Storage storage) {
        ResourceType selected = getSelectedResource();
        int qty = quantitySection.getValue();
        if (selected == null || qty <= 0) {
            summaryLabel.setText(" ");
            sendButton.setEnabled(false);
            return;
        }
        if (storage.getQuantity(selected) < qty) {
            summaryLabel.setText("Not enough " + selected.name());
            summaryLabel.setForeground(UITheme.NEGATIVE_CHANGE);
            sendButton.setEnabled(false);
            return;
        }

        int relation = computeRelationGain(selected, qty);
        summaryLabel.setText("Give " + qty + " " + selected.name() + "  ->  +" + relation + " Relation");
        summaryLabel.setForeground(UITheme.POSITIVE_CHANGE);
        sendButton.setEnabled(true);
    }

    public static int computeRelationGain(ResourceType type, int amount) {
        return switch (type) {
            case WOOD, FOOD -> (int) Math.floor(amount / 10.0 * 2);
            case STONE -> (int) Math.floor(amount / 10.0 * 3);
            case IRON -> (int) Math.floor(amount / 5.0 * 3);
        };
    }

    public ResourceType getSelectedResource() {
        int i = resourceSection.getSelectedIndex();
        return i >= 0 ? TYPES[i] : null;
    }

    public int getQuantity() {
        return quantitySection.getValue();
    }

    public OptionSection getResourceSection() {
        return resourceSection;
    }

    public SliderSection getQuantitySection() {
        return quantitySection;
    }

    public JButton getSendButton() {
        return sendButton;
    }
}
