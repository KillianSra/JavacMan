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
    private final int scatterModeBeginningWorldX = 576;
    private final int scatterModeBeginningWorldY = 96;

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

        if(alternationNumber != 6)
        {
            handleModeAlternation(scatterModeBeginningWorldX, scatterModeBeginningWorldY);
        }

        //Pathfinding
        if(mode == Mode.CHASE && transitionBetweenMode)
        {
            //Transition between Chase mode and Scatter mode.
            int col = scatterModeBeginningWorldX / gp.tileSize;
            int row = scatterModeBeginningWorldY / gp.tileSize;
            searchPath(col, row);
        }
        else if(mode == Mode.CHASE || mode == Mode.EATEN)
        {
            pathfinding();
        }
        else if(mode == Mode.SCATTER)
        {
            scatterMode();
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
        spriteAnimation();
    }

    @Override
    public void getImage()
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
        if(scatterPhase == 0)
        {
            this.direction = Direction.LEFT;
            if(worldX == 480)
            {
                scatterPhase++;
            }
        }
        else if(scatterPhase == 1)
        {
            this.direction = Direction.DOWN;
            if(worldY == 144)
            {
                worldY -= speed;
                hitbox.y -= speed;
                scatterPhase++;
            }
        }
        else if(scatterPhase == 2)
        {
            this.direction = Direction.RIGHT;
            if(worldX == 576)
            {
                scatterPhase++;
            }
        }
        else if(scatterPhase == 3)
        {
            this.direction = Direction.UP;
            if(worldY == 96)
            {
                scatterPhase = 0;
            }
        }
    }
}
