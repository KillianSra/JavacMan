package storage.controls;

import java.io.*;

public class ControlsManager
{
    private static final String FILE_NAME = System.getProperty("user.home") + "/javacman/controls.dat";
    private static final int UP_KEYCODE = 90;
    private static final int DOWN_KEYCODE = 83;
    private static final int LEFT_KEYCODE = 81;
    private static final int RIGHT_KEYCODE = 68;
    private static final int PAUSE_KEYCODE = 27;

    /**
     * Ensures that the controls file exists with a default values.
     * If the file does not exist, it creates one with a default configuration :
     * <ul>
     *     <li>Up = Z</li>
     *     <li>Down = S</li>
     *     <li>Left = Q</li>
     *     <li>Right = D</li>
     *     <li>Pause = ESC</li>
     * </ul>
     * <p>
     *     In order for these values to be used by the KeyListener, the keycodes are stored.
     * </p>
     */
    public static void initializeControls()
    {
        File file = new File(FILE_NAME);

        if (!file.exists())
        {

            //Create the folder if necessary
            file.getParentFile().mkdirs();

            //Create the file with default settings
            save(UP_KEYCODE, DOWN_KEYCODE, LEFT_KEYCODE, RIGHT_KEYCODE, PAUSE_KEYCODE);
        }
    }

    /**
     * Saves the given settings to a file named "controls.dat".
     *
     * @param up The up keycode.
     * @param down The down keycode.
     * @param left The left keycode.
     * @param right The right keycode.
     * @param pause The pause keycode.
     * @throws RuntimeException if an error occurs while saving the controls.
     */
    public static void save(int up, int down, int left, int right, int pause)
    {
        try
        {
            //Create an ObjectOutputStream to write objects to a file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FILE_NAME)));

            //Create a Controls object to hold the settings configuration
            Controls controls = new Controls(up, down, left, right, pause);

            //Write the Settings object to the file
            oos.writeObject(controls);

            //Close the stream to release resources
            oos.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to save the controls : ", e);
        }
    }

    /**
     * Loads the saved controls from the file.
     *
     * @return The stored {@link Controls} object.
     * @throws RuntimeException if an error occurs while loading the controls.
     */
    public static Controls load()
    {
        Controls controls;

        try
        {
            //Create an ObjectInputStream to read objects from the file
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FILE_NAME)));

            //Read the Controls object from the file
            controls = (Controls) ois.readObject();

            //Close the stream to release resources
            ois.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to load the controls : ", e);
        }

        //Return the stored controls object
        return controls;
    }
}
