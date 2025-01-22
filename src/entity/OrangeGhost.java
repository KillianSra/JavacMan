package entity;

import main.GamePanel;

import java.awt.*;

public class OrangeGhost extends Entity implements Ghost
{
    private boolean scatterModeOn = false;
    private int scatterPhase = 0;

    public OrangeGhost(GamePanel gp)
    {
        super(gp);

        //Hitbox settings
        hitbox = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);

        setStartPosition();
        getImage();
    }

    //Methods
    public void setStartPosition()
    {
        direction = Direction.LEFT;
        worldX = gp.tileSize * 15;
        worldY = gp.tileSize * 13;
        speed = 2;

        //Synchronize hitbox with entity position
        hitbox.x = worldX;
        hitbox.y = worldY;
    }

    @Override
    public void update()
    {
        //Check event
        gp.eventManager.checkEvent(this);

        //If the orange close is close enough from the player, start chasing them.
        if(getTileDistanceFromPlayer(gp.player) < 8)
        {
            //Reset scatter mode properties
            scatterModeOn = false;
            scatterPhase = 0;

            //Finds the best path to reach the player
            int goalCol = gp.player.getWorldX() / gp.tileSize;
            int goalRow = gp.player.getWorldY() / gp.tileSize;
            searchPath(goalCol, goalRow);
        }
        //The ghost is too far from the player
        else
        {
            //The ghost heads to the bottom left corner
            if((worldX != 96 || worldY != 552) && !scatterModeOn)
            {
                //Reach the bottom-left corner
                searchPath(4, 23);
            }
            //The ghost has reached the bottom left corner, it goes into scatter mode
            else
            {
                scatterModeOn = true;
            }
        }

        if(scatterModeOn)
        {
            scatterMode();
        }

        //Check collisions
        gp.collisionManager.checkTileCollision(this);
        gp.collisionManager.checkEntityCollision(gp.player, this);

        if(!isCollision())
        {
            super.move();
        }

        //Handle ghost's sprite animation
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
        up1 = setup("orange/orange_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("orange/orange_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("orange/orange_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("orange/orange_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("orange/orange_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("orange/orange_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("orange/orange_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("orange/orange_right_2", gp.tileSize, gp.tileSize);
    }

    @Override
    public void scatterMode()
    {
        if(scatterPhase == 0)
        {
            this.direction = Direction.RIGHT;
            if(worldX == 240)
            {
                worldX -= speed;
                hitbox.x -= speed;
                scatterPhase++;
            }
        }
        else if (scatterPhase == 1)
        {
            this.direction = Direction.UP;
            if(worldY == 504)
            {
                worldY += speed;
                hitbox.y += speed;
                scatterPhase++;
            }
        }
        else if (scatterPhase == 2)
        {
            this.direction = Direction.RIGHT;
            if(worldX == 312)
            {
                worldX -= speed;
                hitbox.x -= speed;
                scatterPhase++;
            }
        }
        else if (scatterPhase == 3)
        {
            this.direction = Direction.UP;
            if(worldY == 456)
            {
                worldY += speed;
                hitbox.y += speed;
                scatterPhase++;
            }
        }
        else if (scatterPhase == 4)
        {
            this.direction = Direction.LEFT;
            if(worldX == 96)
            {
                worldX += speed;
                hitbox.x += speed;
                scatterPhase++;
            }
        }
        else if (scatterPhase == 5)
        {
            this.direction = Direction.DOWN;
            if(worldY == 552)
            {
                worldY -= speed;
                hitbox.y -= speed;
                scatterPhase = 0;
            }
        }
    }
}