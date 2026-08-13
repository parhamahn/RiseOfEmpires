package model.disasters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NaturalDisaster {

    public enum DisasterType {EARTHQUAKE, FLOOD, TSUNAMI, BEAR_ATTACK}

    public static List<DisasterType> getRandomDisasters(Random rand){
        List<DisasterType> disasters = new ArrayList<DisasterType>();

        for(DisasterType disasterType : DisasterType.values()){
            if(rand.nextDouble()<=0.05){
                disasters.add(disasterType);
            }
        }

        return  disasters;
    }

}
