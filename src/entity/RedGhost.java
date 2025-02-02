package entity;

import entity.abstracts.Entity;
import entity.enums.Direction;
import entity.enums.Mode;
import entity.interfaces.Ghost;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class RedGhost extends Entity implements Ghost
{
    private final int spawnCol = 14;
    private final int spawnRow = 11;

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
        worldX = gp.tileSize * spawnCol;
        worldY = gp.tileSize * spawnRow;
        speed = 2;
        defaultSpeed = speed;

        //Synchronize hitbox with entity position
        hitbox.x = worldX;
        hitbox.y = worldY;
    }

    @Override
    public void pathfinding()
    {
        if(mode == Mode.EATEN)
        {
            eatenBehavior(spawnCol, spawnRow);
        }
        //A* algorithm
        else
        {
            int goalCol = gp.player.getWorldX() / gp.tileSize;
            int goalRow = gp.player.getWorldY() / gp.tileSize;
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

        if(!isCollision())
        {
            //If there is no collision, move in the new direction
            super.move();
        }

        //Check collisions
        gp.collisionManager.checkTileCollision(this);
        gp.collisionManager.checkEntityCollision(gp.player, this);

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
