package view.buildingpanel.townhall;

import model.queue.Queue;
import model.queue.ResearchQueue;
import model.queue.Technology;
import model.state.GameState;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ResearchSection extends JPanel {

    private final GameState gameState;
    private final Map<Technology, ResearchStat> researchStatMap;
    private final List<Technology> techOrder;

    public ResearchSection(GameState gameState) {
        this.gameState = gameState;
        researchStatMap = new EnumMap<>(Technology.class);
        techOrder = new ArrayList<>();
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel label = new JLabel("Technology Research");
        label.setFont(UITheme.GAME_FONT.deriveFont(32f));
        label.setForeground(UITheme.GOLD);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 15)));

        buildInitial();
    }

    public JButton getResearchButton(Technology tech) {
        ResearchStat stat = researchStatMap.get(tech);
        return stat != null ? stat.getResearchButton() : null;
    }

    public void refresh() {
        for (Technology tech : Technology.values()) {
            ResearchStat stat = researchStatMap.get(tech);
            if (stat == null) continue;

            boolean alreadyResearched = gameState.getEmpireState().getResearchedTechnologies().contains(tech);
            boolean canAfford = tech.getCost().enoughResources(gameState.getEmpireState().getStorage()) && tech.getTownHallLevel().ordinal()<=gameState.getEmpireState().getTownHallLevel().ordinal() && gameState.getEmpireState().getQueues().isEmpty();
            boolean alreadyQueued = gameState.getEmpireState().getQueues().stream()
                    .anyMatch(q -> q.getQueueType() == Queue.QUEUE_TYPE.RESEARCH
                            && ((ResearchQueue) q).getTechnology() == tech);

            stat.refresh(canAfford, alreadyResearched || alreadyQueued);
        }
        revalidate();
        repaint();
    }

    private void buildInitial() {
        for (Technology tech : Technology.values()) {
            boolean alreadyResearched = gameState.getEmpireState().getResearchedTechnologies().contains(tech);
            boolean canAfford = tech.getCost().enoughResources(gameState.getEmpireState().getStorage());
            boolean alreadyQueued = gameState.getEmpireState().getQueues().stream()
                    .anyMatch(q -> q.getQueueType() == Queue.QUEUE_TYPE.RESEARCH
                            && ((ResearchQueue) q).getTechnology() == tech);

            String iconPath = iconPathFor(tech);
            ResearchStat stat = new ResearchStat(tech, iconPath, canAfford, alreadyResearched || alreadyQueued);
            researchStatMap.put(tech, stat);
            techOrder.add(tech);
            stat.setAlignmentX(CENTER_ALIGNMENT);
            add(stat);
            add(Box.createRigidArea(new Dimension(0, 5)));
        }

        add(Box.createVerticalGlue());
    }

    private String iconPathFor(Technology technology) {
        return switch (technology) {
            case SAILING -> "/utils/sailingresearch.png";
            case STEEL_TOOLS -> "/utils/steelresearch.png";
            case DEFENSIVE_PATTERN -> "/utils/defensivepatternresearch.png";
        };
    }
}
