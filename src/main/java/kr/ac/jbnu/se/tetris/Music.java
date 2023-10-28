package kr.ac.jbnu.se.tetris;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {

    private File file;
    private Clip clip;

    Music() {
        setMusic();
    }

    public void setMusic() {
        file = new File("bgm/bgm.wav");
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setVolume(float volume){
        if(clip != null){
            if (volume < 0f) volume = 0f;
            else if (volume > 1f) volume = 1f;
            FloatControl VolumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            VolumeControl.setValue(dB);
        }
    }

    public void stopMusic() {
        clip.stop();
    }

    public void startMusic() {
        clip.start();
        setVolume(0.3f);
        clip.loop(10);
    }

}


