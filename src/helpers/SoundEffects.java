package helpers;

public class SoundEffects {

    private static final SoundEffects instance = new SoundEffects();
    private final Sound selectHexSoundEffect;

    private SoundEffects() {
        selectHexSoundEffect = new Sound();
        selectHexSoundEffect.load("/utils/selecthex.wav");
        selectHexSoundEffect.setVolume(1f);
    }

    public static SoundEffects getInstance(){
        return instance;
    }

    public void playSelectHexSoundEffect(){
        selectHexSoundEffect.playOnce();
    }

}
