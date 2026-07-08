package model.queue;

public class ResearchQueue extends Queue {

    private Technology technology;


    public ResearchQueue(Technology technology, int turnCost, int startTurn){
        super(QUEUE_TYPE.RESEARCH, turnCost, startTurn);
        this.technology = technology;
    }


    public Technology getTechnology() {
        return technology;
    }
}
