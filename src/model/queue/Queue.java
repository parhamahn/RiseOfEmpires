package model.queue;

import model.Entity;
import model.state.GameState;

import java.io.Serializable;

public abstract class Queue extends Entity implements Serializable {
private static final long serialVersionUID = 1L;

    public enum QUEUE_TYPE {UPGRADE, RESEARCH, COOK};

    private int turnCost;
    private int startTurn;
    private int delay;
    private QUEUE_TYPE queueType;


    public Queue(QUEUE_TYPE queueType, int turnCost, int startTurn){
        super();
        this.queueType = queueType;
        this.turnCost = turnCost;
        this.startTurn = startTurn;
        this.delay = 0;
    }


    public int getTurnCost() {
        return turnCost;
    }

    public int getStartTurn() {
        return startTurn;
    }

    public QUEUE_TYPE getQueueType() {
        return queueType;
    }

    public boolean isComplete(int currentTurn) {
        return startTurn + turnCost + delay <= currentTurn;
    }

    public int getTurnsLeft(int currentTurn) {
        return Math.max(0, (startTurn + turnCost + delay) - currentTurn);
    }

    public void addDelay(int turns) {
        this.delay += turns;
    }


    public abstract boolean execute(GameState gameState);

    public abstract void cancel(GameState gameState);

    public abstract String getDescription();

    protected String formatted(Enum<?> value) {
        StringBuilder formatted = new StringBuilder();
        boolean nextUpper = true;
        for (char c : value.name().toCharArray()) {
            if (c == '_') {
                formatted.append(' ');
                nextUpper = true;
            } else if (nextUpper) {
                formatted.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                formatted.append(Character.toLowerCase(c));
            }
        }
        return formatted.toString();
    }
}
