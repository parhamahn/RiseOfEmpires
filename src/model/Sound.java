package model;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private Clip clip;
    private FloatControl volumeControl;
    private float currentVolume = 0.7f;

    public void load(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Audio file not found: " + path);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
            Mixer selectedMixer = null;
            for (Mixer.Info mixerInfo : mixerInfos) {
                if (mixerInfo.getName().contains("PCH [plughw:1,0]")) {
                    selectedMixer = AudioSystem.getMixer(mixerInfo);
                    break;
                }
            }
            if (selectedMixer == null) {
                for (Mixer.Info mixerInfo : mixerInfos) {
                    Mixer m = AudioSystem.getMixer(mixerInfo);
                    if (m.isLineSupported(info)) {
                        selectedMixer = m;
                        break;
                    }
                }
            }
            if (selectedMixer == null) {
                System.err.println("No usable mixer found");
                return;
            }

            clip = (Clip) selectedMixer.getLine(info);
            clip.open(audioStream);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(currentVolume);
            }
            clip.setFramePosition(0);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void setVolume(float percent) {
        currentVolume = percent;
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float dB = min + (max - min) * percent;
            volumeControl.setValue(dB);
        }
    }

    public float getVolume() {
        return currentVolume;
    }
}