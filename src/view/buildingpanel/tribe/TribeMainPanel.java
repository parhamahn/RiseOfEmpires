package view.buildingpanel.tribe;

import helpers.ImageLoader;
import model.state.GameState;
import view.common.BaseModalPanel;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class TribeMainPanel extends BaseModalPanel{

    private JButton sendGiftButton;
    private JButton startTrade;
    private JButton getMission;
    private JButton doMission;
    private JButton declareWar;
    private JButton askPeace;
    private JButton askAlliance;

    protected TribeMainPanel(GameState gameState, String title, String backgroundP) {
        super(gameState, title, backgroundP);
        ImageIcon upgradeIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 200, 56);

    }

    @Override
    protected JPanel buildBody() {
        return null;
    }

    @Override
    public void refresh() {

    }
}
