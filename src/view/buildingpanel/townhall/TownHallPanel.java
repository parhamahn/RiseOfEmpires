package view.buildingpanel.townhall;

import model.map.Hex;
import model.queue.Queue;
import model.queue.Technology;
import model.state.GameState;
import model.unit.Unit;
import view.common.BaseModalPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class TownHallPanel extends BaseModalPanel {

    private Hex sourceHex;
    private UpgradeSection upgradeSection;
    private CookSection cookSection;
    private QueueSection queueSection;
    private ResearchSection researchSection;

    public TownHallPanel(GameState gameState) {
        super(gameState, "Town Hall", "/utils/townhallpanel.png");
    }

    @Override
    protected JPanel buildBody() {
        JPanel body = new JPanel(new GridLayout(2, 2, 15, 15));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        upgradeSection = new UpgradeSection(getGameState());
        cookSection = new CookSection(getGameState());
        researchSection = new ResearchSection(getGameState());
        queueSection = new QueueSection(getGameState());

        body.add(upgradeSection);
        body.add(cookSection);
        body.add(researchSection);
        body.add(queueSection);

        return body;
    }

    public JButton getCookButton(Unit.UNIT_TYPE type) {return cookSection.getCookButton(type);}
    public Map<Unit.UNIT_TYPE, JButton> getCookButtons() {return cookSection.getCookButtons();}
    public JButton getUpgradeStorageButton() {return upgradeSection.getUpgradeButton();}
    public JButton getResearchButton(Technology tech) {return researchSection.getResearchButton(tech);}
    public void setQueue(List<Queue> queues) {queueSection.setQueue(queues);}
    public void setOnCancel(java.util.function.Consumer<Queue> onCancel) {queueSection.setOnCancel(onCancel);}
    public List<QueueStat> getQueueStats() {return queueSection.getCurrentStats();}

    public Hex getSourceHex() { return sourceHex; }
    public void setSourceHex(Hex sourceHex) { this.sourceHex = sourceHex; }

    @Override
    public void refresh() {
        upgradeSection.refresh();
        cookSection.refresh(sourceHex);
        researchSection.refresh();
        setQueue(getGameState().getEmpireState().getQueues());
    }
}
