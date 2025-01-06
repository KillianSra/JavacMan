package main;

import annotation.DebugOnly;
import entity.Direction;

import java.awt.*;

public class EventManager
{
    private final int EVENT_NUMBER = 4;
    GamePanel gp;
    Rectangle[] eventRectangles = new Rectangle[EVENT_NUMBER];
    int[] triggersX = new int[EVENT_NUMBER];
    int[] triggersY = new int[EVENT_NUMBER];

    public EventManager(GamePanel gp)
    {
        this.gp = gp;

        setTriggers();

        for(int i = 0; i < EVENT_NUMBER; i++)
        {
            eventRectangles[i] = new Rectangle();
            eventRectangles[i].x = triggersX[i];
            eventRectangles[i].y = triggersY[i];
            eventRectangles[i].width = 2;
            eventRectangles[i].height = gp.tileSize;
        }
    }

    /**
     * Manually set the location of trigger events
     */
    private void setTriggers()
    {
        triggersX[0] = gp.tileSize * 4;
        triggersY[0] = gp.tileSize * 9;

        triggersX[1] = gp.tileSize * 4;
        triggersY[1] = gp.tileSize * 15;

        triggersX[2] = gp.tileSize * 25;
        triggersY[2] = gp.tileSize * 9;

        triggersX[3] = gp.tileSize * 25;
        triggersY[3] = gp.tileSize * 15;
    }

    /**
     * Checks for specific events based on the player's interaction with triggers.
     */
    public void checkEvent()
    {
        if(hit(0, Direction.LEFT)) { teleport(gp.tileSize * 24, gp.tileSize * 9); }
        else if(hit(1, Direction.LEFT)) { teleport(gp.tileSize * 24, gp.tileSize * 15); }
        else if(hit(2, Direction.RIGHT)) { teleport(gp.tileSize * 4, gp.tileSize * 9); }
        else if(hit(3, Direction.RIGHT)) { teleport(gp.tileSize * 4, gp.tileSize * 15); }
    }

    /**
     * Checks if the player has triggered a specific event based on their position and direction.
     *
     * @param triggerIndex Index of the event rectangle to check against.
     * @param requiredDirection The direction the player must face to trigger the event.
     * @return true if the event is triggered; false otherwise.
     */
    private boolean hit(int triggerIndex, Direction requiredDirection)
    {
        boolean hit = false;

        //Get the player's hitbox and check if it intersects with the event rectangle at the specified index.
        Rectangle playerHitbox = gp.player.getHitbox();
        if(playerHitbox.intersects(eventRectangles[triggerIndex]))
        {
            //Ensure the player is facing the required direction to activate the event.
            if(gp.player.getDirection() == requiredDirection)
            {
                hit = true;
            }
        }

        return hit;
    }

    /**
     * Teleports the player to a specific location on the map.
     *
     * @param x The x-coordinate to teleport the player to.
     * @param y The y-coordinate to teleport the player to.
     */
    private void teleport(int x, int y)
    {
        gp.player.setWorldX(x);
        gp.player.setWorldY(y);
    }

    @DebugOnly
    public void draw(Graphics2D g2)
    {
        for(int i = 0; i < this.EVENT_NUMBER; i++)
        {
            g2.fillRect(eventRectangles[i].x, eventRectangles[i].y, eventRectangles[i].width, eventRectangles[i].height);
        }
    }
}