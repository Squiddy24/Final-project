package main;

//Imports needed for class
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioManager {

    //A list of sounds that will be played throughout the game
    URL[] soundURLs = new URL[4];

    //The current sound clip to be played
    Clip clip;

    //Constructor sets all elements in the sounds URL array
    public AudioManager(){
        soundURLs[0] = getClass().getResource("/main/Audio/MainTheme.wav");
        soundURLs[1] = getClass().getResource("/main/Audio/Dash.wav");
        soundURLs[2] = getClass().getResource("/main/Audio/Hit.wav");
        soundURLs[3] = getClass().getResource("/main/Audio/LevelCompleat.wav");

    }

    //Loads up the current file to be played
    public void SetFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    //Plays the clip
    public void play(){
        clip.start();
    }

    //Loops the clip
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
