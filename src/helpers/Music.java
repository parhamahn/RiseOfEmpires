package helpers;

public class Music {
    private static Music instance;
    private Sound backgroundMusic;
    
    private Music() {
        backgroundMusic = new Sound();
    }
    
    public static Music getInstance() {
        if (instance == null) {
            instance = new Music();
            instance.loadBackgroundMusic("/utils/music.wav");
        }
        return instance;
    }
    
    public void loadBackgroundMusic(String path) {
        backgroundMusic.load(path);
    }
    
    public void setVolume(float percent) {
        backgroundMusic.setVolume(percent);
    }
    
}
