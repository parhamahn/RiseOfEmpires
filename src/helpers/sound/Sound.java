package helpers.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    private Clip clip;
    private FloatControl volumeControl;

    public void load(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Audio file not found: " + path);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            audioStream.close();

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void playOnce() {
        if (clip != null) {
            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void setVolume(float percent) {
        if (volumeControl == null) return;
        percent = Math.max(0f, Math.min(1f, percent));
        float dB = (percent == 0f) ? volumeControl.getMinimum()
                : (float) (Math.log10(percent) * 10.0);
        dB = Math.max(volumeControl.getMinimum(), Math.min(volumeControl.getMaximum(), dB));
        volumeControl.setValue(dB);
    }

    public void close() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}