package entity.abstracts;

import annotation.DebugOnly;
import entity.enums.Direction;
import entity.Player;
import entity.enums.Mode;
import main.GamePanel;
import main.Renderable;
import tile.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Entity extends Renderable
{
    protected GamePanel gp;
    protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public int hitboxDefaultX;
    public int hitboxDefaultY;
    protected int frightenedMaxTime = 1000;
    public static int GHOSTS_EATEN_IN_A_ROW = 0;

    //Constant
    private final static int POINT = 200;
    private final int CHASE_MODE_DURATION = 1200;             //20 seconds
    private final int SCATTER_MODE_DURATION = 420;            //7 seconds
    private final int FINAL_SCATTER_MODE_DURATION = 300;      //5 seconds

    //State
    protected Direction direction = Direction.LEFT;
    protected Mode mode = Mode.CHASE;
    protected int spriteNum = 1;
    protected boolean eatenImageLoaded = false;
    protected int worldXDead;
    protected int worldYDead;
    protected boolean displayPointsWon = false;
    protected int scatterPhase = 0;
    protected int alternationNumber = 0;
    protected boolean transitionBetweenMode = false;

    //Counter
    protected int spriteCounter;
    protected int frightenedCounter = 0;
    protected int changeDirectionCounter = 0;
    protected int modeAlternationCounter = 0;

    //Attribute
    public int speed;
    public int defaultSpeed;

    public Entity(GamePanel gp)
    {
        this.gp = gp;
    }

    //Getters
    public int getSpeed() { return this.speed; }
    public Direction getDirection() { return this.direction; }
    public BufferedImage getLeft2() { return this.left2; }
    public Mode getMode() { return this.mode; }
    public boolean getDisplayPointsWon() { return this.displayPointsWon; }
    public int getWorldXDead() { return this.worldXDead; }
    public int getWorldYDead() { return this.worldYDead; }

    //Setters
    public void setDirection(Direction direction) { this.direction = direction; }
    public void setMode(Mode mode) { this.mode = mode; }
    public void setDisplayPointsWon(boolean displayPointsWon) { this.displayPointsWon = displayPointsWon; }
    public void setWorldXDead(int x) { this.worldXDead = x; }
    public void setWorldYDead(int y) { this.worldYDead = y; }

    public void resetFrightenedCounter(){ this.frightenedCounter = 0; }

    //Abstract method
    /**
     * Updates the state or behavior of the entity.
     */
    public abstract void update();

    //Methods

    @Override
    public void draw(Graphics2D g2)
    {
        g2.drawImage(setDisplayedImage(), worldX, worldY, null);

        if(gp.isDebuggingEnabled)
        {
            drawEntityHitbox(g2);
        }
    }

    /**
     * Determines the appropriate image to display based on the entity's current direction
     * and animation frame (sprite number).
     *
     * @return A BufferedImage representing the current frame of the entity's animation.
     */
    protected BufferedImage setDisplayedImage()
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

    /**
     * Reads frightened images and stores them in BufferedImage
     */
    protected void getFrightenedImage()
    {
        up1 = setup("frightened/frightened_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("frightened/frightened_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("frightened/frightened_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("frightened/frightened_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("frightened/frightened_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("frightened/frightened_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("frightened/frightened_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("frightened/frightened_right_2", gp.tileSize, gp.tileSize);
    }

    /**
     * Reads eaten images and stores them in BufferedImage
     */
    protected void getEatenImage()
    {
        up1 = setup("eaten/eaten_up", gp.tileSize, gp.tileSize);
        up2 = setup("eaten/eaten_up", gp.tileSize, gp.tileSize);
        down1 = setup("eaten/eaten_down", gp.tileSize, gp.tileSize);
        down2 = setup("eaten/eaten_down", gp.tileSize, gp.tileSize);
        left1 = setup("eaten/eaten_left", gp.tileSize, gp.tileSize);
        left2 = setup("eaten/eaten_left", gp.tileSize, gp.tileSize);
        right1 = setup("eaten/eaten_right", gp.tileSize, gp.tileSize);
        right2 = setup("eaten/eaten_right", gp.tileSize, gp.tileSize);
    }

    protected void move()
    {
        switch(this.direction)
        {
            case Direction.UP: worldY = worldY - speed; break;
            case Direction.DOWN: worldY = worldY + speed; break;
            case Direction.LEFT: worldX = worldX - speed; break;
            case Direction.RIGHT: worldX = worldX + speed; break;
        }

        //Synchronize hitbox with entity position
        hitbox.x = worldX;
        hitbox.y = worldY;
    }

    /**
     * Manages the behavior of the ghost entity during the FRIGHTENED mode.
     */
    protected void checkFrightened()
    {
        if(mode == Mode.FRIGHTENED && frightenedCounter == 0)
        {
            getFrightenedImage();
            frightenedCounter++;
            GHOSTS_EATEN_IN_A_ROW = 0;
        }
        else if(mode == Mode.FRIGHTENED)
        {
            frightenedCounter++;
        }
        if(frightenedCounter == frightenedMaxTime)
        {
            getImage();
            mode = Mode.CHASE;
            frightenedCounter = 0;
            speed = defaultSpeed;
        }
    }

    /**
     * Calculates and updates the entity's movement direction to follow the shortest path
     * toward the specified goal position on the grid.
     *
     * @param goalCol the column index of the goal position on the grid
     * @param goalRow the row index of the goal position on the grid
     */
    protected void searchPath(int goalCol, int goalRow)
    {
        int startCol = worldX / gp.tileSize;
        int startRow = worldY / gp.tileSize;

        gp.pathfinder.setNodes(startCol, startRow, goalCol, goalRow);

        if(gp.pathfinder.search())
        {
            //Next worldX & worldY
            int nextX = gp.pathfinder.pathList.getFirst().col * gp.tileSize;
            int nextY = gp.pathfinder.pathList.getFirst().row * gp.tileSize;

            if(hitbox.x > nextX && hitbox.y == nextY)
            {
                direction = Direction.LEFT;
            }
            else if(hitbox.x < nextX && hitbox.y == nextY)
            {
                direction = Direction.RIGHT;
            }
            else if(hitbox.x == nextX && hitbox.y < nextY)
            {
                direction = Direction.DOWN;
            }
            else if(hitbox.x == nextX && hitbox.y > nextY)
            {
                direction = Direction.UP;
            }
        }
    }

    /**
     * Calculates the distance (in tiles) between this entity
     * and the player based on their world coordinates.
     *
     * @param player The player entity used to calculate the distance.
     * @return The distance in tiles between this entity and the player.
     */
    protected int getTileDistanceFromPlayer(Player player)
    {
        int xDist = Math.abs(worldX - player.worldX);
        int yDist = Math.abs(worldY - player.worldY);

        return (xDist + yDist) / gp.tileSize;
    }

    /**
     * Determines the possible directions that an entity can move to based on its current position.
     * The method excludes the opposite direction to prevent U-turns.
     *
     * @param entity the entity for which the possible directions are to be calculated
     * @return a list of possible directions the entity can move to, excluding the opposite direction
     */
    protected ArrayList<Direction> possibleDirections(Entity entity)
    {
        ArrayList<Direction> possibleDirections = new ArrayList<>();
        Direction oppositeDirection = getOppositeDirection(entity);

        //Current entity location
        int col = entity.getWorldX() / gp.tileSize;
        int row = entity.getWorldY() / gp.tileSize;

        int tileIndex;
        Tile tile;

        if(oppositeDirection != Direction.UP)
        {
            tileIndex = gp.tileManager.mapTileNum[col][row - 1];
            tile = gp.tileManager.tiles[tileIndex];
            if(!tile.isCollision())
            {
                possibleDirections.add(Direction.UP);
            }
        }
        if(oppositeDirection != Direction.DOWN)
        {
            tileIndex = gp.tileManager.mapTileNum[col][row + 1];
            tile = gp.tileManager.tiles[tileIndex];
            if(!tile.isCollision())
            {
                possibleDirections.add(Direction.DOWN);
            }
        }
        if(oppositeDirection != Direction.LEFT)
        {
            tileIndex = gp.tileManager.mapTileNum[col - 1][row];
            tile = gp.tileManager.tiles[tileIndex];
            if(!tile.isCollision())
            {
                possibleDirections.add(Direction.LEFT);
            }
        }
        if(oppositeDirection != Direction.RIGHT)
        {
            tileIndex = gp.tileManager.mapTileNum[col + 1][row];
            tile = gp.tileManager.tiles[tileIndex];
            if(!tile.isCollision())
            {
                possibleDirections.add(Direction.RIGHT);
            }
        }

        return possibleDirections;
    }

    /**
     * Determines the opposite direction of the given entity's current direction.
     *
     * @param entity the entity whose opposite direction is to be determined
     * @return the direction opposite to the entity's current direction, or null if no direction is set
     */
    protected Direction getOppositeDirection(Entity entity)
    {
        Direction oppositeDirection = null;
        switch(entity.getDirection())
        {
            case Direction.UP: oppositeDirection = Direction.DOWN; break;
            case Direction.DOWN: oppositeDirection = Direction.UP; break;
            case Direction.LEFT: oppositeDirection = Direction.RIGHT; break;
            case Direction.RIGHT: oppositeDirection = Direction.LEFT; break;
        }

        return oppositeDirection;
    }

    /**
     * Resynchronizes the position of the specified ghost entity to align it
     * with the grid defined by the tile size.
     *
     * @param ghost The ghost entity whose position needs to be resynchronized.
     *              The entity's current direction determines the adjustment.
     */
    protected void resynchronizePosition(Entity ghost)
    {
        switch(ghost.getDirection())
        {
            case UP:
                while(ghost.worldY % gp.tileSize != 0)
                {
                    worldY++;
                }
                break;
            case DOWN:
                while(ghost.worldY % gp.tileSize != 0)
                {
                    worldY--;
                }
                break;
            case LEFT:
                while (ghost.worldX % gp.tileSize != 0)
                {
                    worldX++;
                }
                break;
            case RIGHT:
                while (ghost.worldX % gp.tileSize != 0)
                {
                    worldX--;
                }
                break;
        }
    }

    /**
     * Handles the behavior of the ghost when it has been eaten by the player.
     * <p>
     * This method manages the transition of the ghost to the "eaten" state, guiding it
     * back to its spawn point and restoring its previous mode once it arrives. During this state,
     * the ghost's speed and appearance are adjusted accordingly.
     *
     * @param spawnCol the column index of the spawn point on the map
     * @param spawnRow the row index of the spawn point on the map
     */
    protected void eatenBehavior(int spawnCol, int spawnRow)
    {
        //Return to the spawn point
        searchPath(spawnCol, spawnRow);
        if(!eatenImageLoaded)
        {
            getEatenImage();
            eatenImageLoaded = true;
            speed = defaultSpeed;
            //Correct the position if necessary
            resynchronizePosition(this);
        }
        //If the spawn point has been reached, return to the previous mode
        else if(spawnCol == worldX / gp.tileSize && spawnRow == worldY / gp.tileSize)
        {
            mode = Mode.CHASE;
            getImage();
            eatenImageLoaded = false;
        }
    }

    @Override
    public void checkDisplayedPoint()
    {
        displayPointsWonCounter++;
        if(displayPointsWonCounter == POINT_DISPLAYED_TIME)
        {
            displayPointsWon = false;
            displayPointsWonCounter = 0;
        }
    }

    /**
     * Retrieves the duration of the current ghost mode.
     *
     * @return The duration of the current mode (FPS * duration)
     */
    protected int getModeDuration()
    {
        int duration = 0;
        if(this.mode == Mode.CHASE)
        {
            duration = CHASE_MODE_DURATION;
        }
        else if(this.mode == Mode.SCATTER && alternationNumber == 5)
        {
            duration = FINAL_SCATTER_MODE_DURATION;
        }
        else if(this.mode == Mode.SCATTER)
        {
            duration = SCATTER_MODE_DURATION;
        }

        return duration;
    }

    /**
     * Calculates the points earned when eating a ghost.
     * The points increase exponentially based on the number of ghosts eaten in a row.
     *
     * @return The total points awarded for eating the current ghost.
     */
    public static int calculatePointsWon()
    {
        int points = POINT;

        switch(GHOSTS_EATEN_IN_A_ROW)
        {
            case 2: points *= 2; break;
            case 3: points *= 4; break;
            case 4: points *= 8; break;
        }

        return points;
    }

    /**
     * Manages the alternation between CHASE and SCATTER modes.
     * The mode alternates based on a duration counter. If the mode is CHASE,
     * the entity transitions to SCATTER mode when reaching the specified (x, y) position.
     * Otherwise, it switches back to CHASE mode after the SCATTER duration ends.
     *
     * @param x The target x-coordinate required for the mode transition.
     * @param y The target y-coordinate required for the mode transition.
     */
    public void handleModeAlternation(int x, int y)
    {
        int duration = getModeDuration();

        if(modeAlternationCounter < duration)
        {
            modeAlternationCounter++;
        }
        else
        {
            switch(mode)
            {
                case CHASE:
                    transitionBetweenMode = true;
                    if(worldX == x && worldY == y)
                    {
                        mode = Mode.SCATTER;
                        modeAlternationCounter = 0;
                        transitionBetweenMode = false;
                        alternationNumber++;
                        scatterPhase = 0;
                    }
                    break;

                case SCATTER:
                    mode = Mode.CHASE;
                    modeAlternationCounter = 0;
                    alternationNumber++;
                    break;
            }
        }
    }

    /**
     * Resets the mode alternation state.
     */
    public void resetAlternation()
    {
        modeAlternationCounter = 0;
        alternationNumber = 0;
        transitionBetweenMode = false;
    }

    @DebugOnly
    private void drawEntityHitbox(Graphics2D g2)
    {
        g2.setColor(Color.WHITE);

        //Display entity's hitbox
        g2.drawRect(getHitboxX(), getHitboxY(), getHitboxWidth(), getHitboxHeight());
    }
}