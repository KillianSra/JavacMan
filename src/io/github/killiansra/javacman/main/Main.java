package io.github.killiansra.javacman.main;

import javax.swing.*;
import java.awt.*;

public class Main
{
    public final static JFrame WINDOW = new JFrame();
    private final static GamePanel GAME_PANEL = new GamePanel();
    public final static GraphicsDevice DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            //Window configuration
            WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            WINDOW.setResizable(false);
            WINDOW.setTitle("JavacMan");

            WINDOW.add(GAME_PANEL);

            WINDOW.pack();
            WINDOW.setLocationRelativeTo(null);
            WINDOW.setVisible(true);

            //Start game loop
            GAME_PANEL.startGameThread();
        });
    }

    /**
     * Toggles between fullscreen and windowed mode.
     */
    public static void toggleFullScreen()
    {
        if(!GAME_PANEL.fullScreen)
        {
            //Default size
            DEVICE.setFullScreenWindow(null);
            WINDOW.dispose();
            WINDOW.setUndecorated(false);
            WINDOW.setVisible(true);
        }
        else
        {
            setFullScreen();
        }

        GAME_PANEL.defineWindowDimension();
    }

    /**
     * Switches the game window to fullscreen mode.
     */
    public static void setFullScreen()
    {
        //Full screen mode
        WINDOW.dispose();
        WINDOW.setUndecorated(true);
        DEVICE.setFullScreenWindow(WINDOW);

        GAME_PANEL.screenHeightFS = Main.DEVICE.getFullScreenWindow().getHeight();

        //Adjust screen width dynamically while maintaining the original aspect ratio (672:696).
        //This ensures the game scales proportionally regardless of the screen resolution.
        GAME_PANEL.screenWidthFS = (int) (GAME_PANEL.screenHeightFS * (672.0 / 696.0));
    }
}