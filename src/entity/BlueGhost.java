package entity;

import entity.abstracts.Entity;
import entity.enums.Direction;
import entity.enums.Mode;
import entity.interfaces.Ghost;
import main.GamePanel;

import java.awt.*;

public class BlueGhost extends Entity implements Ghost
{
    private final int scatterModeBeginningWorldX = 576;
    private final int scatterModeBeginningWorldY = 552;
    private final int spawnTime = 420;      //7 seconds

    public BlueGhost(GamePanel gp)
    {
        super(gp);

        //Hitbox settings
        hitbox = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);

        spawnCol = 14;
        spawnRow = 13;

        setStartPosition();
        getImage();
    }

    //Methods
    public void setStartPosition()
    {
        direction = Direction.UP;
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
        //Handle the behavior of the blue ghost in game
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
        if(scatterPhase == 0)
        {
            this.direction = Direction.LEFT;
            if(worldX == 432)
            {
                worldX += speed;
                hitbox.x += speed;
                scatterPhase++;
            }
        }
        else if(scatterPhase == 1)
        {
            this.direction = Direction.UP;
            if(worldY == 504)
            {
                scatterPhase++;
            }
        }
        else if(scatterPhase == 2)
        {
            this.direction = Direction.LEFT;
            if(worldX == 360)
            {
                scatterPhase++;
            }
        }
        else if(scatterPhase == 3)
        {
            this.direction = Direction.UP;
            if(worldY == 456)
            {
                scatterPhase++;
            }
        }
        else if(scatterPhase == 4)
        {
            this.direction = Direction.RIGHT;
            if(worldX == 576)
            {
                scatterPhase++;
            }
        }
        else if(scatterPhase == 5)
        {
            this.direction = Direction.DOWN;
            if(worldY == 552)
            {
                scatterPhase = 0;
            }
        }
    }
}
