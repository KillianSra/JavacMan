package entity;

import annotation.DebugOnly;
import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity
{
    private final KeyHandler keyHandler;

    //Attribute
    private int life = 3;
    private int score = 0;

    public Player(GamePanel gp, KeyHandler keyHandler)
    {
        super(gp);
        this.keyHandler = keyHandler;

        //Hitbox settings
        hitbox = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setStartPosition();
        getImage();
    }

    //Getters
    public int getScore() { return this.score; }
    public int getLife() { return this.life; }

    //Setters
    public void setScore(int score) { this.score = score; }
    public void setLife(int life) { this.life = life; }

    //Methods
    public void setStartPosition()
    {
        direction = Direction.LEFT;
        worldX = gp.tileSize * 14;
        worldY = gp.tileSize * 19;
        speed = 1;
        defaultSpeed = speed;
    }

    @Override
    public void update()
    {
        //Check event
        gp.eventManager.checkEvent();

        //Store the current direction
        Direction previousDirection = getDirection();

        //handles the player's movements on the game grid based on the key pressed
        if(keyHandler.upPressed) { setDirection(Direction.UP); }
        else if(keyHandler.downPressed) { setDirection(Direction.DOWN); }
        else if(keyHandler.leftPressed) { setDirection(Direction.LEFT); }
        else if(keyHandler.rightPressed) { setDirection(Direction.RIGHT); }

        //Check collisions
        gp.collisionManager.checkTileCollision(this);
        gp.collisionManager.checkObjectCollision(this, gp.objects);

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

            //Move in the previous direction if there's no collision
            if (!isCollision())
            {
                super.move();
            }
            //Otherwise, reset the counter and stop the animation
            else
            {
                spriteNum = 2;
                spriteCounter = 0;
            }
        }

        //Handle player's sprite animation
        spriteCounter++;
        if(spriteCounter > 20)
        {
            if(spriteNum == 1)
            {
                spriteNum = 2;
            }
            else if(spriteNum == 2)
            {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    @Override
    protected void getImage()
    {
        up1 = setup("player/player_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("player/player_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("player/player_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("player/player_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("player/player_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("player/player_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("player/player_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("player/player_right_2", gp.tileSize, gp.tileSize);
    }

    /**
     * Determines the appropriate image to display based on the entity's current direction
     * and animation frame (sprite number).
     *
     * @return A BufferedImage representing the current frame of the entity's animation.
     */
    private BufferedImage setDisplayedImage()
    {
        BufferedImage displayedImage = null;

        //Select the appropriate sprite based on the direction and sprite number
        switch(direction)
        {
            case Direction.UP:
                if(spriteNum == 1) { displayedImage = up1; }
                else if(spriteNum == 2) { displayedImage = up2; }
                break;
            case Direction.DOWN:
                if(spriteNum == 1) { displayedImage = down1; }
                else if(spriteNum == 2) { displayedImage = down2; }
                break;
            case Direction.LEFT:
                if(spriteNum == 1) { displayedImage = left1; }
                else if(spriteNum == 2) { displayedImage = left2; }
                break;
            case Direction.RIGHT:
                if(spriteNum == 1) { displayedImage = right1; }
                else if(spriteNum == 2) { displayedImage = right2; }
                break;
        }

        return displayedImage;
    }

    @Override
    public void draw(Graphics2D g2)
    {
        g2.drawImage(setDisplayedImage(), worldX, worldY, null);

        if(gp.isDebuggingEnabled)
        {
            drawCoordinatesAndPlayerHitbox(g2);
        }

    }

    @DebugOnly
    private void drawCoordinatesAndPlayerHitbox(Graphics2D g2)
    {
        g2.setColor(Color.WHITE);

        //Display player's hitbox
        g2.drawRect(getHitboxX(), getHitboxY(), getHitboxWidth(), getHitboxHeight());

        //Display player coordinates
        g2.drawString("x: " + getWorldX(), gp.tileSize * 2, gp.tileSize * 25);
        g2.drawString("y: " + getWorldY(), gp.tileSize * 2, gp.tileSize * 26);
        g2.drawString("col: " + getWorldX() / gp.tileSize, gp.tileSize * 2, gp.tileSize * 27);
        g2.drawString("row: " + getWorldY() / gp.tileSize, gp.tileSize * 2, gp.tileSize * 28);
    }
}
