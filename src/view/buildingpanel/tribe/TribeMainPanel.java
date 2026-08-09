package view.buildingpanel.tribe;

import model.building.tribes.Tribe;
import model.state.GameState;
import view.common.BaseModalPanel;


import javax.swing.*;
import java.awt.*;

public class TribeMainPanel extends BaseModalPanel{

    private TribeInfoSection tribeInfoSection;
    private TribeActionSection tribeActionSection;
    private Tribe tribe;

    public TribeMainPanel(GameState gameState) {
        super(gameState, "Tribe", "/utils/bazaarpanel.png");
    }

    @Override
    protected JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 15));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(0, 50, 40, 50));

        tribeInfoSection = new TribeInfoSection();
        tribeActionSection = new TribeActionSection(getGameState());

        body.add(tribeInfoSection, BorderLayout.WEST);
        body.add(tribeActionSection, BorderLayout.EAST);

        return body;
    }

    @Override
    public void refresh() {
        tribeActionSection.reset(tribe);
        tribeInfoSection.reset(tribe);
    }

    public TribeInfoSection getTribeInfoSection() {
        return tribeInfoSection;
    }

    public TribeActionSection getTribeActionSection() {
        return tribeActionSection;
    }

    public Tribe getTribe() {
        return tribe;
    }

    public void setTribe(Tribe tribe) {
        this.tribe = tribe;
    }
}
