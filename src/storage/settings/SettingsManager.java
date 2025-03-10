package storage.settings;

import java.io.*;

public class SettingsManager
{
    private static final String FILE_NAME = "src/storage/settings/settings.dat";

    /**
     * Ensures that the settings file exists with a default values.
     * If the file does not exist, it creates one with a default configuration :
     * <ul>
     *     <li>volumeScale = 3</li>
     *     <li>fullScreen = false</li>
     *     <li>displayFPSCounter = false</li>
     * </ul>
     */
    public static void initializeSettings()
    {
        File file = new File(FILE_NAME);

        if (!file.exists())
        {
            //Create the file with default settings
            save(3, false, false);
        }
    }

    /**
     * Saves the given settings to a file named "settings.dat".
     *
     * @param volumeScale The volume scale to be saved.
     * @param fullScreen The boolean value to know if the game should be in full screen.
     * @param displayFPSCounter The boolean value to know if the FPS counter should be displayed.
     * @throws RuntimeException if an error occurs while saving the settings.
     */
    public static void save(int volumeScale, boolean fullScreen, boolean displayFPSCounter)
    {
        try
        {
            //Create an ObjectOutputStream to write objects to a file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FILE_NAME)));

            //Create a Settings object to hold the settings configuration
            Settings settings = new Settings(volumeScale, fullScreen, displayFPSCounter);

            //Write the Settings object to the file
            oos.writeObject(settings);

            //Close the stream to release resources
            oos.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to save the settings : ", e);
        }
    }

    /**
     * Loads the saved settings from the file.
     *
     * @return The stored {@link Settings} object.
     * @throws RuntimeException if an error occurs while loading the settings.
     */
    public static Settings load()
    {
        Settings settings;

        try
        {
            //Create an ObjectInputStream to read objects from the file
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FILE_NAME)));

            //Read the Storage object from the file
            settings = (Settings) ois.readObject();

            //Close the stream to release resources
            ois.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to load the settings : ", e);
        }

        //Return the stored settings object
        return settings;
    }
}