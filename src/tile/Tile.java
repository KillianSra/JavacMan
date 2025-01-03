package tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile
{
    private BufferedImage image;
    private boolean collision;

    public BufferedImage getImage()
    {
        return image;
    }

    public boolean isCollision()
    {
        return collision;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public void setCollision(boolean collision)
    {
        this.collision = collision;
    }

    /**
     * Scales the given BufferedImage to the specified width and height.
     *
     * @param original The original BufferedImage to be scaled.
     * @param width The desired width of the scaled image.
     * @param height The desired height of the scaled image.
     * @return A new BufferedImage object representing the scaled image.
     */
    public BufferedImage scaleImage(BufferedImage original, int width, int height)
    {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }
}
