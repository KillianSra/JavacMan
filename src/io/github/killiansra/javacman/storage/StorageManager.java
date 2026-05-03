package io.github.killiansra.javacman.storage;

import java.nio.file.Path;

public class StorageManager
{
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String DIR_NAME = getDirNameByOSConvention();

    //Private constructor to prevent accidental instantiation
    private StorageManager(){}

    /**
     * Returns the application directory name following OS-specific naming conventions.
     *
     * @return the directory name adapted to the current OS
     */
    private static String getDirNameByOSConvention()
    {
        return (OS.contains("win") || OS.contains("mac")) ? "JavacMan" : "javacman";
    }

    /**
     * Returns the platform-specific path to a user configuration directory
     *
     * @return the absolute path to the config directory as a string
     */
    public static String getUserConfigPath()
    {
        Path baseDir;

        //Windows
        if(OS.contains("win"))
            baseDir = Path.of(System.getenv("APPDATA"), DIR_NAME);
        //MacOs
        else if(OS.contains("mac"))
            baseDir = Path.of(System.getProperty("user.home"), "Library", "Preferences", DIR_NAME);
        //Linux, UNIX-like
        else {
            String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");
            //Check XDG Config Home
            if(xdgConfigHome != null && !xdgConfigHome.isBlank())
                baseDir = Path.of(xdgConfigHome, DIR_NAME);
            else
                baseDir = Path.of(System.getProperty("user.home"), ".config", DIR_NAME);
        }

        return baseDir.toString();
    }

    /**
     * Returns the platform-specific path to a user data directory
     *
     * @return the absolute path to the data directory as a string
     */
    public static String getUserDataPath()
    {
        Path baseDir;

        //Windows
        if(OS.contains("win"))
            baseDir = Path.of(System.getenv("LOCALAPPDATA"), DIR_NAME);
        //MacOs
        else if(OS.contains("mac"))
            baseDir = Path.of(System.getProperty("user.home"), "Library", "Application Support", DIR_NAME);
        //Linux, Unix-like
        else {
            String xdgDataHome = System.getenv("XDG_DATA_HOME");
            //Check XDG Data Home
            if(xdgDataHome != null && !xdgDataHome.isBlank())
                baseDir = Path.of(xdgDataHome, DIR_NAME);
            else
                baseDir = Path.of(System.getProperty("user.home"), ".local", "share", DIR_NAME);
        }

        return baseDir.toString();
    }
}
