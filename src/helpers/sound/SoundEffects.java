package helpers.sound;

import model.Season;
import model.state.GameState;

public class SoundEffects {

    private static final SoundEffects instance = new SoundEffects();
    private final Sound selectHexSoundEffect;
    private final Sound startGameSoundEffect;
    private final Sound rainSoundEffect;
    private final Sound birdSoundEffect;
    private final Sound hoverSoundEffect;
    private final Sound unitMoveSoundEffect;

    private SoundEffects() {
        selectHexSoundEffect = new Sound();
        selectHexSoundEffect.load("/utils/selecthex.wav");
        selectHexSoundEffect.setVolume(1f);

        startGameSoundEffect = new Sound();
        startGameSoundEffect.load("/utils/startgame.wav");
        startGameSoundEffect.setVolume(1f);

        rainSoundEffect = new Sound();
        rainSoundEffect.load("/utils/rainloop.wav");
        rainSoundEffect.setVolume(1f);

        birdSoundEffect = new Sound();
        birdSoundEffect.load("/utils/birdsing.wav");
        birdSoundEffect.setVolume(0.25f);

        hoverSoundEffect = new Sound();
        hoverSoundEffect.load("/utils/hover.wav");
        hoverSoundEffect.setVolume(1f);

        unitMoveSoundEffect = new Sound();
        unitMoveSoundEffect.load("/utils/unitmove.wav");
        unitMoveSoundEffect.setVolume(0.5f);
    }

    public static SoundEffects getInstance(){
        return instance;
    }

    public void playSelectHexSoundEffect(){
        selectHexSoundEffect.playOnce();
    }
    public void playStartGameSoundEffect(){startGameSoundEffect.playOnce();}
    public void startRainloopSoundEffect(){rainSoundEffect.play();}
    public void stopRainloopSoundEffect(){rainSoundEffect.stop();}
    private void startBirdSoundEffect(){birdSoundEffect.play();}
    private void stopBirdSoundEffect(){birdSoundEffect.stop();}
    public void playHoverSoundEffect(){hoverSoundEffect.playOnce();}

    public void seasonSoundEffect(Season.SeasonType old, Season.SeasonType newSeason){
        if(newSeason== Season.SeasonType.FALL){
            if(old != Season.SeasonType.FALL){SoundEffects.getInstance().startRainloopSoundEffect();}
        }else{
            SoundEffects.getInstance().stopRainloopSoundEffect();
        }
        if(newSeason== Season.SeasonType.SPRING){
            if(old!=Season.SeasonType.SPRING){SoundEffects.getInstance().startBirdSoundEffect();}
        }else{
            SoundEffects.getInstance().stopBirdSoundEffect();
        }
    }

    public void stopEffects(){
        rainSoundEffect.stop();
        birdSoundEffect.stop();
    }

    public void playUnitMoveSoundEffect(){unitMoveSoundEffect.playOnce();}

}
