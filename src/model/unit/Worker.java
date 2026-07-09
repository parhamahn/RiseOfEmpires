package model.unit;

public class Worker extends Unit{

    public Worker(){super(UNIT_TYPE.WORKER, 5, 2, 10);}

    @Override
    public boolean isWorker() { return true; }
}
