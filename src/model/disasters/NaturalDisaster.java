package model.disasters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NaturalDisaster {

    public enum DisasterType {EARTHQUAKE, FLOOD, TSUNAMI}

    public static List<DisasterType> getRandomDisasters(){
        List<DisasterType> disasters = new ArrayList<DisasterType>();
        Random rand = new Random();

        for(DisasterType disasterType : DisasterType.values()){
            if(rand.nextDouble()<=0.05){
                disasters.add(disasterType);
            }
        }

        return  disasters;
    }

}
