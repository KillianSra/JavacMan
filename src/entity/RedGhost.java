package entity;

import main.GamePanel;

import java.awt.*;

public class RedGhost extends Entity implements Ghost
{
    public RedGhost(GamePanel gp)
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
        worldX = gp.tileSize * 14;
        worldY = gp.tileSize * 11;
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

        int goalCol = gp.player.getWorldX() / gp.tileSize;
        int goalRow = gp.player.getWorldY() / gp.tileSize;
        searchPath(goalCol, goalRow);

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
        up1 = setup("red/red_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("red/red_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("red/red_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("red/red_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("red/red_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("red/red_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("red/red_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("red/red_right_2", gp.tileSize, gp.tileSize);
    }

    @Override
    public void scatterMode()
    {
        //TODO
    }
}
