package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;

public class Player extends Entity
{
    private final KeyHandler keyHandler;

    public Player(GamePanel gp, KeyHandler keyHandler)
    {
        super(gp);
        this.keyHandler = keyHandler;

        //Hitbox settings
        hitbox = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setStartPosition();
    }

    public void setStartPosition()
    {
        worldX = gp.tileSize * 4;
        worldY = gp.tileSize * 5;
        speed = 1;
        defaultSpeed = speed;
    }

    @Override
    public void update()
    {
        //Store the current direction
        Direction previousDirection = getDirection();

        //handles the player's movements on the game grid based on the key pressed
        if(keyHandler.upPressed) { setDirection(Direction.UP); }
        else if(keyHandler.downPressed) { setDirection(Direction.DOWN); }
        else if(keyHandler.leftPressed) { setDirection(Direction.LEFT); }
        else if(keyHandler.rightPressed) { setDirection(Direction.RIGHT); }

        //Check collisions
        gp.collisionManager.checkTileCollision(this);

        if(!isCollision())
        {
            //If there is no collision, move in the new direction
            super.move();
        }
        else
        {
            // If there's a collision, revert to the previous direction and re-check
            setDirection(previousDirection);
            gp.collisionManager.checkTileCollision(this);

            // Move in the previous direction if there's no collision
            if (!isCollision())
            {
                super.move();
            }
        }
    }

    @Override
    public void draw(Graphics2D g2)
    {
        //PLACEHOLDER
        //This will be replaced with the player's sprites
        g2.setColor(Color.YELLOW);
        g2.fillOval(getWorldX(), getWorldY(), gp.tileSize, gp.tileSize);


        //DEBUG

        g2.setColor(Color.WHITE);
        //Display player's hitbox
        //g2.drawRect(getHitboxX(), getHitboxY(), getHitboxWidth(), getHitboxHeight());

        //Display player coordinates
        g2.drawString("x: " + getWorldX(), gp.tileSize * 2, gp.tileSize * 25);
        g2.drawString("y: " + getWorldY(), gp.tileSize * 2, gp.tileSize * 26);
        g2.drawString("col: " + getWorldX() / gp.tileSize, gp.tileSize * 2, gp.tileSize * 27);
        g2.drawString("row: " + getWorldY() / gp.tileSize, gp.tileSize * 2, gp.tileSize * 28);
    }
}
