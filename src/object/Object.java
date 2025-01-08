package object;

import annotation.DebugOnly;
import main.GamePanel;
import main.Renderable;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Object extends Renderable
{
    private final int LIFE_SPAN = 600;     //10 seconds

    protected GamePanel gp;
    protected BufferedImage image;
    private final int point;
    private final boolean hasLimitedLifeSpan;
    protected int arrayIndex;

    private int lifeSpanCounter = 0;

    public Object(GamePanel gp, int point, boolean hasLimitedLifeSpan)
    {
        this.gp = gp;
        this.point = point;
        this.hasLimitedLifeSpan = hasLimitedLifeSpan;
    }

    //Getter
    public BufferedImage getObjectImage() { return this.image; }
    public int getPoint() { return this.point; }
    public boolean getHasLimitedLifeSpan() { return this.hasLimitedLifeSpan; }

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

    /**
     * Increments the life span counter and checks if the object has reached its maximum life span.
     * If the life span has been reached, the object is removed .
     *
     * @param index The index of the object in the objects array to check and potentially remove.
     */
    public void checkLifeSpan(int index)
    {
        lifeSpanCounter++;
        if(lifeSpanCounter == LIFE_SPAN)
        {
            gp.objects[index] = null;
            lifeSpanCounter = 0;
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