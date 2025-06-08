package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioHandler {

    URL[] soundURLs = new URL[4];
    Clip clip;

    public AudioHandler(){
        soundURLs[0] = getClass().getResource("/main/Audio/MainTheme.wav");
        soundURLs[1] = getClass().getResource("/main/Audio/Dash.wav");
        soundURLs[2] = getClass().getResource("/main/Audio/Hit.wav");
        soundURLs[3] = getClass().getResource("/main/Audio/LevelCompleat.wav");

    }

    public void SetFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }

}
