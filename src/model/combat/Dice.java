package model.combat;

import java.util.Random;

public class Dice {

    private Random rand = new Random();
    private static Dice instance;
    private Dice(){};

    public static Dice getInstance(){
        if(instance == null){
            instance = new Dice();
        }
        return instance;
    }

    public int roll(){
        return rand.nextInt(6)+1;
    }

}
