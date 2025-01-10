package main;

import object.*;

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
     * @return A new BufferedImage object representing the scaled image.
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
     * Returns an instance of the object corresponding to the given name.
     *
     * @param gp   The GamePanel instance required to create the object.
     * @param name The name of the object to create.
     * @return A new instance of the corresponding object, or null if no match is found.
     */
    public static object.Object getObjectByName(GamePanel gp, String name)
    {
        object.Object obj = null;

        switch(name)
        {
            case Bean.name : obj = new Bean(gp, 0, 0, 0); break;
            case Coffee.name : obj = new Coffee(gp, 0, 0, 0); break;
            case Computer.name : obj = new Computer(gp, 0, 0, 0); break;
            case IDE.name : obj = new IDE(gp, 0, 0, 0); break;
            case Lightbulb.name : obj = new Lightbulb(gp, 0, 0, 0); break;
            case Mug.name : obj = new Mug(gp, 0, 0, 0); break;
        }

        return obj;
    }
}
