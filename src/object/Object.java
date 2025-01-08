package object;

import annotation.DebugOnly;
import main.GamePanel;
import main.Renderable;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Object extends Renderable
{
    protected GamePanel gp;
    protected BufferedImage image;
    private final int point;

    public Object(GamePanel gp, int point)
    {
        this.gp = gp;
        this.point = point;
    }

    //Getter
    public BufferedImage getObjectImage() { return this.image; }
    public int getPoint() { return this.point; }

    @Override
    public void draw(Graphics2D g2)
    {
        g2.drawImage(image, worldX, worldY, null);

        //DEBUG
        if(gp.isDebuggingEnabled)
        {
            drawObjectHitbox(g2);
        }
    }

    @DebugOnly
    private void drawObjectHitbox(Graphics2D g2)
    {
        g2.setColor(Color.WHITE);

        //Display object's hitbox
        g2.drawRect(getHitboxX(), getHitboxY(), getHitboxWidth(), getHitboxHeight());
    }
}