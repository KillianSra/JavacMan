package settings;

import java.io.Serializable;

/**
 * A record that stores settings values.
 * <p>
 * This record is immutable and implements {@link Serializable}, allowing it
 * to be saved and loaded from a file.
 * </p>
 *
 * @param volumeScale        The volume level scale.
 * @param fullScreen         A boolean indicating whether the game should start in fullscreen mode.
 * @param displayFPSCounter  A boolean indicating whether the FPS counter should be displayed on the screen.
 */
public record Settings(int volumeScale, boolean fullScreen, boolean displayFPSCounter) implements Serializable {}