package io.github.killiansra.javacman.main;

import io.github.killiansra.javacman.object.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tools
{
    /**
     * Scales the given BufferedImage to the specified width and height.
     *
     * @param original The original BufferedImage to be scaled.
     * @param width The desired width of the scaled image.
     * @param height The desired height of the scaled image.
     * @return A new BufferedImage io.github.killiansra.javacman.object representing the scaled image.
     */
    public static BufferedImage scaleImage(BufferedImage original, int width, int height)
    {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }

    /**
     * Returns an instance of the io.github.killiansra.javacman.object corresponding to the given name.
     *
     * @param gp   The GamePanel instance required to create the io.github.killiansra.javacman.object.
     * @param name The name of the io.github.killiansra.javacman.object to create.
     * @return A new instance of the corresponding io.github.killiansra.javacman.object, or null if no match is found.
     */
    public static io.github.killiansra.javacman.object.Object getObjectByName(GamePanel gp, String name)
    {
        io.github.killiansra.javacman.object.Object obj = null;

        switch(name)
        {
            case Bean.name : obj = new Bean(gp); break;
            case Coffee.name : obj = new Coffee(gp); break;
            case Computer.name : obj = new Computer(gp); break;
            case IDE.name : obj = new IDE(gp); break;
            case Lightbulb.name : obj = new Lightbulb(gp); break;
            case Mug.name : obj = new Mug(gp); break;
        }

        return obj;
    }
}
