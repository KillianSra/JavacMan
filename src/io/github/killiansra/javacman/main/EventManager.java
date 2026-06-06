package io.github.killiansra.javacman.main;

import io.github.killiansra.javacman.annotation.DebugOnly;
import io.github.killiansra.javacman.entity.enums.Direction;
import io.github.killiansra.javacman.entity.abstracts.Entity;

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
     * Checks for specific events based on the targeted io.github.killiansra.javacman.entity's interaction with triggers.
     */
    public void checkEvent(Entity entity)
    {
        if(hit(entity, 0, Direction.LEFT)) { teleport(entity, gp.tileSize * 24, gp.tileSize * 9); }
        else if(hit(entity, 1, Direction.LEFT)) { teleport(entity, gp.tileSize * 24, gp.tileSize * 15); }
        else if(hit(entity, 2, Direction.RIGHT)) { teleport(entity, gp.tileSize * 4, gp.tileSize * 9); }
        else if(hit(entity, 3, Direction.RIGHT)) { teleport(entity, gp.tileSize * 4, gp.tileSize * 15); }
    }

    /**
     * Checks if the targeted io.github.killiansra.javacman.entity has triggered a specific event based on their position and direction.
     *
     * @param entity the targeted io.github.killiansra.javacman.entity
     * @param triggerIndex Index of the event rectangle to check against.
     * @param requiredDirection The direction the player must face to trigger the event.
     * @return true if the event is triggered; false otherwise.
     */
    private boolean hit(Entity entity, int triggerIndex, Direction requiredDirection)
    {
        boolean hit = false;

        //Get the io.github.killiansra.javacman.entity's hitbox and check if it intersects with the event rectangle at the specified index.
        Rectangle entityHitbox = entity.getMovementHitbox();
        if(entityHitbox.intersects(eventRectangles[triggerIndex]))
        {
            //Ensure the player is facing the required direction to activate the event.
            if(entity.getDirection() == requiredDirection)
            {
                hit = true;
            }
        }

        return hit;
    }

    /**
     * Teleports the player to a specific location on the map.
     *
     * @param entity The io.github.killiansra.javacman.entity to be teleported
     * @param x The x-coordinate to teleport the io.github.killiansra.javacman.entity to.
     * @param y The y-coordinate to teleport the io.github.killiansra.javacman.entity to.
     */
    private void teleport(Entity entity, int x, int y)
    {
        entity.setWorldX(x);
        entity.setWorldY(y);
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