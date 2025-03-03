package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound
{
    //Properties
    private Clip clip;
    private URL[] soundUrl = new URL[9];
    private FloatControl floatControl;
    public int volumeScale = 3;
    private float volume;

    //Constants
    public static final int MENU_NAVIGATION = 0;
    public static final int MENU_SELECTION = 1;
    public static final int PICK_UP = 2;
    public static final int PICK_UP_ITEMS = 3;
    public static final int POWER_UP = 4;
    public static final int EAT_GHOST = 5;
    public static final int HIT = 6;
    public static final int GAME_OVER = 7;
    public static final int BAR_MOVEMENT = 8;

    public Sound()
    {
        soundUrl[0] = getClass().getResource("/sound/menuNavigation.wav");
        soundUrl[1] = getClass().getResource("/sound/menuSelection.wav");
        soundUrl[2] = getClass().getResource("/sound/pickUp.wav");
        soundUrl[3] = getClass().getResource("/sound/pickUpItems.wav");
        soundUrl[4] = getClass().getResource("/sound/powerUp.wav");
        soundUrl[5] = getClass().getResource("/sound/eatGhost.wav");
        soundUrl[6] = getClass().getResource("/sound/hit.wav");
        soundUrl[7] = getClass().getResource("/sound/gameOver.wav");
        soundUrl[8] = getClass().getResource("/sound/barMovement.wav");
    }

    /**
     * Loads an audio file from the specified index.
     *
     * @param i the index of the audio file in the {@code soundUrl} array.
     * @throws RuntimeException if the audio file cannot be opened due to an unsupported format,
     *                          I/O error, or unavailable audio line.
     */
    public void setFile(int i)
    {
        try
        {
            //Open audio file
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

            //Handle volume level
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolumeLevel();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            throw new RuntimeException("Failed to open the sound file : " + e);
        }
    }

    /**
     * Plays the currently loaded audio file.
     */
    public void play()
    {
        clip.start();
    }

    /**
     * Sets the volume level based on the predefined scale.
     * The volume scale ranges from 0 (mute) to 5 (maximum volume).
     */
    public void setVolumeLevel()
    {
        switch(volumeScale)
        {
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break;
        }

        floatControl.setValue(volume);
    }
}
