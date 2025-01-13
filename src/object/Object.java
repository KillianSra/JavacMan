package object;

import annotation.DebugOnly;
import main.GamePanel;
import main.Renderable;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Object extends Renderable
{
    private final int LIFE_SPAN = 600;     //10 seconds
    private final int DISPLATED_TEXT = 180;     //3 seconds

    protected GamePanel gp;
    protected BufferedImage image;
    private final int point;
    private final boolean hasLimitedLifeSpan;
    protected int arrayIndex;
    protected boolean displayPoint;
    private boolean delete = false;

    //Counters
    private int lifeSpanCounter = 0;
    private int displayPointCounter = 0;

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
    public boolean getDisplayPoint() { return this.displayPoint; }
    public boolean getDelete() { return this.delete; }
    public abstract String getName();

    //Setter
    public void setDisplayPoint(boolean displayPoint) { this.displayPoint = displayPoint; }

    @Override
    public void draw(Graphics2D g2)
    {
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD));

        if(!displayPoint && !delete)
        {
            g2.drawImage(image, worldX, worldY, null);
        }
        //If the player took the collectible
        else
        {
            int pointWorldX = worldX + 2;
            int pointWorldY = worldY + (gp.tileSize / 2) + (g2.getFont().getSize() / 2);
            //Display points awarded
            g2.drawString(String.valueOf(point), pointWorldX, pointWorldY);
        }

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

    /**
     * Tracks the display duration of points on the screen.
     * Increments the counter until it reaches the predefined limit.
     */
    public void checkDisplayedPoint()
    {
        displayPointCounter++;
        if(displayPointCounter == DISPLATED_TEXT)
        {
            displayPoint = false;
            displayPointCounter = 0;
            delete = true;
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