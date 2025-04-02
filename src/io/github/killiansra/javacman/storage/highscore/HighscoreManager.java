package io.github.killiansra.javacman.storage.highscore;

import java.io.*;

public class HighscoreManager
{
    private static final String FILE_NAME = System.getProperty("user.home") + "/javacman/highestScore.dat";

    /**
     * Ensures that the score file exists with a default value.
     * If the file does not exist, it creates one with a default score of 0.
     */
    public static void initializeStorage()
    {
        File file = new File(FILE_NAME);

        if (!file.exists())
        {
            //Create the folder if necessary
            file.getParentFile().mkdirs();

            //Create the file with default score 0
            save(0);
        }
    }

    /**
     * Saves the given score to a file named "highestScore.dat".
     *
     * @param score The score to be saved.
     * @throws RuntimeException if an error occurs while saving the score.
     */
    public static void save(int score)
    {
        try
        {
            //Create an ObjectOutputStream to write objects to a file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FILE_NAME)));

            //Create a Storage io.github.killiansra.javacman.object to hold the score
            Highscore highscore = new Highscore(score);

            //Write the Storage io.github.killiansra.javacman.object to the file
            oos.writeObject(highscore);

            //Close the stream to release resources
            oos.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to save the score : ", e);
        }
    }

    /**
     * Loads the saved score from the file.
     *
     * @return The stored score.
     * @throws RuntimeException if an error occurs while loading the score.
     */
    public static int load()
    {
        Highscore highscore;

        try
        {
            //Create an ObjectInputStream to read objects from the file
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FILE_NAME)));

            //Read the Storage io.github.killiansra.javacman.object from the file
            highscore = (Highscore) ois.readObject();

            //Close the stream to release resources
            ois.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to load the score : ", e);
        }

        //Return the stored score
        return highscore.score();
    }
}
