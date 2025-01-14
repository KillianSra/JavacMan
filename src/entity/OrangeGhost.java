package entity;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class OrangeGhost extends Entity
{
    //Counter
    int directionCounter = 0;

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
        gp.eventManager.checkEvent();

        if(directionCounter == 100)
        {
            int indexDirection = new Random().nextInt(4);
            switch(indexDirection)
            {
                case 0: setDirection(Direction.UP); break;
                case 1: setDirection(Direction.DOWN); break;
                case 2: setDirection(Direction.LEFT); break;
                case 3: setDirection(Direction.RIGHT); break;
            }
            directionCounter = 0;
        }
        directionCounter++;

        if(!isCollision())
        {
            //If there is no collision, move in the new direction
            super.move();
        }

        //Check collisions
        gp.collisionManager.checkTileCollision(this);
        gp.collisionManager.checkEntityCollision(gp.player, this);

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
}
