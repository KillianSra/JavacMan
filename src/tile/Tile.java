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
}
