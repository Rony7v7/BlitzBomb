package ui;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundController {
    private String path;
    private boolean isLoop;

    public SoundController(String path, boolean isLoop) {
        this.path = path;
        this.isLoop = isLoop;
    }

    public String getPath() {
        return path;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public void play(float volume) { // volumen entre -80 y 6
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            // Control de volumen
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume); // Reduce el volumen en decibelios.

            if (isLoop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException  | javax.sound.sampled.LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
