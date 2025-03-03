package data;

import java.io.*;

public class ScoreManager
{
    private static final String FILE_NAME = "src/data/highestScore.dat";

    /**
     * Ensures that the score file exists with a default value.
     * If the file does not exist, it creates one with a default score of 0.
     */
    public static void initializeStorage()
    {
        File file = new File(FILE_NAME);

        if (!file.exists())
        {
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

            //Create a Storage object to hold the score
            Storage storage = new Storage(score);

            //Write the Storage object to the file
            oos.writeObject(storage);

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
        Storage storage;

        try
        {
            //Create an ObjectInputStream to read objects from the file
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FILE_NAME)));

            //Read the Storage object from the file
            storage = (Storage) ois.readObject();

            //Close the stream to release resources
            ois.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to load the score : ", e);
        }

        //Return the stored score
        return storage.score();
    }
}
