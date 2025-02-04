package entity;

import entity.abstracts.Entity;
import entity.enums.Direction;
import entity.enums.Mode;
import entity.interfaces.Ghost;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BlueGhost extends Entity implements Ghost
{
    private final int spawnCol = 14;
    private final int spawnRow = 13;

    public BlueGhost(GamePanel gp)
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
        worldX = gp.tileSize * spawnCol;
        worldY = gp.tileSize * spawnRow;
        speed = 2;
        defaultSpeed = speed;

        //Synchronize hitbox with entity position
        hitbox.x = worldX;
        hitbox.y = worldY;
    }

    public void pathfinding()
    {
        if(mode == Mode.EATEN)
        {
            eatenBehavior(spawnCol, spawnRow);
        }
        //A* algorithm
        else
        {
            int refPointX = gp.player.getWorldX();
            int refPointY = gp.player.getWorldY();
            switch(gp.player.getDirection())
            {
                case UP: refPointY -= gp.tileSize * 2; break;
                case DOWN: refPointY += gp.tileSize * 2; break;
                case LEFT: refPointX -= gp.tileSize * 2; break;
                case RIGHT: refPointX += gp.tileSize * 2; break;
            }

            int diffX = refPointX - gp.redGhost.getWorldX();
            int diffY = refPointY - gp.redGhost.getWorldY();

            int goalX = gp.redGhost.getWorldX() + diffX * 2;
            int goalY = gp.redGhost.getWorldY() + diffY * 2;

            int goalCol = goalX / gp.tileSize;
            int goalRow = goalY / gp.tileSize;

            if(goalCol > 24)
            {
                goalCol = 24;
            }
            else if(goalCol < 4)
            {
                goalCol = 4;
            }
            if(goalRow > 23)
            {
                goalRow = 23;
            }
            else if(goalRow < 4)
            {
                goalRow = 4;
            }

            searchPath(goalCol, goalRow);
        }
    }

    @Override
    public void update()
    {
        //Check event
        gp.eventManager.checkEvent(this);

        //Pathfinding
        if(mode == Mode.CHASE || mode == Mode.EATEN)
        {
            pathfinding();
        }
        else if(mode == Mode.FRIGHTENED && frightenedCounter == 0)
        {
            this.direction = getOppositeDirection(this);
            speed = 1;
        }
        else if(worldX % gp.tileSize == 0 && worldY % gp.tileSize == 0)
        {
            if(changeDirectionCounter == 1)
            {
                ArrayList<Direction> possibleDirections = possibleDirections(this);
                this.direction = possibleDirections.get(new Random().nextInt(possibleDirections.size()));
                changeDirectionCounter = 0;
            }
            else
            {
                changeDirectionCounter++;
            }
        }

        checkFrightened();

        //Check collisions
        gp.collisionManager.checkTileCollision(this);
        gp.collisionManager.checkEntityCollision(gp.player, this);

        if(!isCollision())
        {
            //If there is no collision, move in the new direction
            super.move();
        }

        if(displayPointsWon)
        {
            checkDisplayedPoint();
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
    public void getImage()
    {
        up1 = setup("blue/blue_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("blue/blue_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("blue/blue_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("blue/blue_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("blue/blue_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("blue/blue_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("blue/blue_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("blue/blue_right_2", gp.tileSize, gp.tileSize);
    }

    @Override
    public void scatterMode()
    {
        //TODO
    }
}
