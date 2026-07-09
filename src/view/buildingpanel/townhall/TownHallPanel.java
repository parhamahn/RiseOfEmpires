package view.buildingpanel.townhall;

import model.queue.Queue;
import model.queue.Technology;
import model.state.GameState;
import model.unit.Unit;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class TownHallPanel extends JPanel {

    private GameState gameState;
    private JButton closeButton;
    private UpgradeSection upgradeSection;
    private CookSection cookSection;
    private QueueSection queueSection;
    private ResearchSection researchSection;

    public TownHallPanel(GameState gameState) {
        this.gameState = gameState;

        setOpaque(false);
        setLayout(new GridBagLayout());
        setVisible(false);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.PANEL_BG);
        content.setPreferredSize(new Dimension(1000, 600));
        content.setBorder(BorderFactory.createLineBorder(UITheme.GOLD, 3));

        content.add(buildTitleBar(), BorderLayout.NORTH);
        content.add(buildBody(), BorderLayout.CENTER);

        add(content);
    }

    public JButton getCloseButton() {return closeButton;}
    public JButton getCookButton(Unit.UNIT_TYPE type) {return cookSection.getCookButton(type);}
    public Map<Unit.UNIT_TYPE, JButton> getCookButtons() {return cookSection.getCookButtons();}
    public JButton getUpgradeStorageButton() {return upgradeSection.getUpgradeButton();}
    public JButton getResearchButton(Technology tech) {return researchSection.getResearchButton(tech);}
    public void setQueue(List<Queue> queues) {queueSection.setQueue(queues);}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(UITheme.OVERLAY_BG);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private JPanel buildTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel title = new JLabel("Town Hall");
        title.setFont(UITheme.GAME_FONT_BOLD.deriveFont(28f));
        title.setForeground(Color.WHITE);
        titleBar.add(title, BorderLayout.WEST);

        closeButton = new JButton("X");
        closeButton.setFocusable(false);
        closeButton.setFont(UITheme.GAME_FONT_BOLD.deriveFont(18f));
        closeButton.setBackground(UITheme.GOLD);
        closeButton.setForeground(Color.BLACK);
        closeButton.setPreferredSize(new Dimension(36, 36));
        closeButton.setMaximumSize(new Dimension(36, 36));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        titleBar.add(closeButton, BorderLayout.EAST);

        return titleBar;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new GridLayout(2, 2, 15, 15));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        upgradeSection = new UpgradeSection(gameState);
        cookSection = new CookSection(gameState);
        researchSection = new ResearchSection(gameState);
        queueSection = new QueueSection(gameState);

        body.add(upgradeSection);
        body.add(cookSection);
        body.add(researchSection);
        body.add(queueSection);

        return body;
    }

    public void open() {refresh();setVisible(true);}
    public void close() {setVisible(false);}

    public void refresh() {
        upgradeSection.refresh();
        cookSection.refresh();
        researchSection.refresh();
        setQueue(gameState.getEmpireState().getQueues());
    }
}
