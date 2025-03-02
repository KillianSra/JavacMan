package entity;

import entity.abstracts.Entity;
import entity.enums.Direction;
import entity.enums.Mode;
import entity.interfaces.Ghost;
import main.GamePanel;

import java.awt.*;

public class OrangeGhost extends Entity implements Ghost
{
    private final int scatterModeBeginningWorldX = 96;
    private final int scatterModeBeginningWorldY = 552;
    private final int spawnTime = 840;      //14 seconds

    private boolean onlyScatter = false;
    private boolean alternationEnabled = true;

    public OrangeGhost(GamePanel gp)
    {
        super(gp);

        //Hitbox settings
        hitbox = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);

        spawnCol = 15;
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
            //If the orange ghost is close enough from the player, start chasing them.
            if(getTileDistanceFromPlayer(gp.player) < 8)
            {
                //Reset scatter mode properties
                mode = Mode.CHASE;
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
                if((worldX != scatterModeBeginningWorldX || worldY != scatterModeBeginningWorldY) && mode != Mode.SCATTER)
                {
                    reachBottomLeftCorner();
                }
                //The ghost has reached the bottom left corner, it goes into scatter mode
                else
                {
                    mode = Mode.SCATTER;
                }
            }
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

            if(alternationNumber != 6 && alternationEnabled)
            {
                handleModeAlternation(scatterModeBeginningWorldX, scatterModeBeginningWorldY);
            }

            //Pathfinding
            if(mode == Mode.CHASE && transitionBetweenMode)
            {
                reachBottomLeftCorner();
            }
            else if(mode == Mode.CHASE || mode == Mode.EATEN)
            {
                pathfinding();
            }
            else if(mode == Mode.SCATTER)
            {
                if(getTileDistanceFromPlayer(gp.player) < 8 && !onlyScatter)
                {
                    this.mode = Mode.CHASE;
                    alternationEnabled = true;
                }
                else
                {
                    //modeAlternationCounter = 0;
                    scatterMode();
                }
            }
            else if(frightenedCounter == 0)
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

        spriteAnimation();
    }

    /**
     * Moves the orange ghost towards the bottom-left corner of the maze.
     */
    private void reachBottomLeftCorner()
    {
        int col = scatterModeBeginningWorldX / gp.tileSize;
        int row = scatterModeBeginningWorldY / gp.tileSize;
        searchPath(col, row);
    }

    @Override
    public void getImage()
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

    @Override
    public void handleModeAlternation(int x, int y)
    {
        int duration = getModeDuration();

        if(modeAlternationCounter < duration)
        {
            modeAlternationCounter++;
        }
        else
        {
            if(mode == Mode.CHASE)
            {
                transitionBetweenMode = true;
                if(worldX == x && worldY == y)
                {
                    mode = Mode.SCATTER;
                    modeAlternationCounter = 0;
                    transitionBetweenMode = false;
                    alternationNumber++;
                    scatterPhase = 0;
                    onlyScatter = true;
                }
            }
            else if(mode == Mode.SCATTER && onlyScatter)
            {
                //Alternation is disabled until a new cycle has been initiated (player <= 8 orange ghost tiles).
                //In the meantime, it will remain in scatter mode
                alternationEnabled = false;

                modeAlternationCounter = 0;
                alternationNumber++;
                onlyScatter = false;
            }
        }
    }
}