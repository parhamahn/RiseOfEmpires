package helpers;

public class SoundEffects {
    
    private static SoundEffects instance = new SoundEffects();
    
    public static SoundEffects getInstance(){
        return instance;
    }
    
    public void playSelectHexSoundEffect(){
        Sound effect = new Sound();
        effect.load("/utils/selecthex.wav");
        effect.setVolume(1f);
        effect.playOnce();
    }
    
}
