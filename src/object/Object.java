package object;

import main.GamePanel;
import main.Renderable;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Object extends Renderable
{
    protected GamePanel gp;
    protected BufferedImage image;

    public Object(GamePanel gp)
    {
        this.gp = gp;
    }

    @Override
    public void draw(Graphics2D g2)
    {
        g2.drawImage(image, worldX, worldY, null);
    }
}