package io.github.killiansra.javacman.main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class Sound
{
    //Properties
    private Clip[][] clips = new Clip[SOUND_NUMBER][INSTANCES_PER_SOUNDS];
    private int[] nextClip = new int[SOUND_NUMBER];
    //private Clip clip;
    private URL[] soundUrl = new URL[SOUND_NUMBER];
    private FloatControl floatControl;
    public int volumeScale = 3;
    private float volume;

    //Constants
    private static final int SOUND_NUMBER = 11;
    private static final int INSTANCES_PER_SOUNDS = 2;

    public static final int MENU_NAVIGATION = 0;
    public static final int MENU_SELECTION = 1;
    public static final int PICK_UP = 2;
    public static final int PICK_UP_ITEMS = 3;
    public static final int POWER_UP = 4;
    public static final int EAT_GHOST = 5;
    public static final int HIT = 6;
    public static final int GAME_OVER = 7;
    public static final int BAR_MOVEMENT = 8;
    public static final int ERROR = 9;
    public static final int LIFE_EARNED = 10;

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
        soundUrl[9] = getClass().getResource("/sound/error.wav");
        soundUrl[10] = getClass().getResource("/sound/lifeEarned.wav");

        loadSounds();
        Arrays.fill(nextClip, 0);
    }

    /**
     * Loads all audio files from resources and creates multiple Clip instances for each sound to allow simultaneous playback.
     *
     * @throws RuntimeException if an audio file cannot be loaded or a clip cannot be created.
     */
    private void loadSounds()
    {
        for(int i = 0; i < soundUrl.length; i++)
        {
            try
            {
                for(int j = 0; j < INSTANCES_PER_SOUNDS; j++)
                {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
                    clips[i][j] = AudioSystem.getClip();
                    clips[i][j].open(ais);
                    ais.close();
                }
            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Plays the specified sound effect.
     */
    public void play(int i)
    {
        Clip clip = clips[i][nextClip[i]];

        floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        setVolumeLevel();

        clip.setFramePosition(0);
        clip.start();

        nextClip[i]++;
        if(nextClip[i] == INSTANCES_PER_SOUNDS) nextClip[i] = 0;
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
