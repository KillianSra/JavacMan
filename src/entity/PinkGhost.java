package entity;

import entity.abstracts.Entity;
import entity.enums.Direction;
import entity.enums.Mode;
import entity.interfaces.Ghost;
import main.GamePanel;

import java.awt.*;

public class PinkGhost extends Entity implements Ghost
{
    private final int scatterModeBeginningWorldX = 96;
    private final int scatterModeBeginningWorldY = 96;
    private final int spawnTime = 60;      //1 second

    public PinkGhost(GamePanel gp)
    {
        super(gp);

        //Hitbox settings
        hitbox = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);

        spawnCol = 13;
        spawnRow = 13;

        setStartPosition();
        getImage();
    }

    //Methods
    public void setStartPosition()
    {
        direction = Direction.DOWN;
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

            switch(gp.player.getDirection())
            {
                case Direction.UP:
                    goalRow -= 4;
                    if(goalRow < gp.minRow)
                    {
                        goalRow = gp.minRow;
                    }
                    break;
                case Direction.DOWN:
                    goalRow += 4;
                    if(goalRow > gp.maxRow)
                    {
                        goalRow = gp.maxRow;
                    }
                    break;
                case Direction.LEFT:
                    goalCol -= 4;
                    if(goalCol < gp.minCol)
                    {
                        goalCol = gp.minCol;
                    }
                    break;
                case Direction.RIGHT:
                    goalCol += 4;
                    if(goalCol > gp.maxRow)
                    {
                        goalCol = gp.maxCol;
                    }
                    break;
            }

            searchPath(goalCol, goalRow);
        }
    }

    @Override
    public void update()
    {
        //Handle the start of the round
        if(!hasSpawn)
        {
            handleSpawn(spawnTime);
        }
        //Handle respawn
        else if(respawning)
        {
            handleRespawn();
        }
        //Handle the behavior of the pink ghost in game
        else
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
                enterFrightenedMode();
            }
            else if(worldX % gp.tileSize == 0 && worldY % gp.tileSize == 0)
            {
                handleRandomMovement();
            }

            checkFrightened();

            handleCollision();

            //Manage the display of points
            if(displayPointsWon)
            {
                checkDisplayedPoint();
            }
        }

        //Handle ghost's sprite animation
        spriteAnimation();
    }

    @Override
    public void getImage()
    {
        up1 = setup("pink/pink_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("pink/pink_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("pink/pink_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("pink/pink_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("pink/pink_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("pink/pink_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("pink/pink_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("pink/pink_right_2", gp.tileSize, gp.tileSize);
    }

    @Override
    public void scatterMode()
    {
        if(scatterPhase == 0)
        {
            this.direction = Direction.RIGHT;
            if(worldX == 192)
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
                hitbox.y = spawnRow;
                scatterPhase++;
            }
        }
        else if(scatterPhase == 2)
        {
            this.direction = Direction.LEFT;
            if(worldX == 96)
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
