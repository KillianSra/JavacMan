package main;

import entity.enums.Direction;
import entity.abstracts.Entity;
import entity.Player;
import entity.enums.Mode;
import object.Javacgum;
import object.Object;
import object.SuperJavacgum;

import java.awt.*;

public class CollisionManager
{
    GamePanel gp;

    public CollisionManager(GamePanel gp)
    {
        this.gp = gp;
    }

    /**
     * Checks for collisions between an entity and solid tiles on the map.
     * Updates the entity's collision state based on the result.
     *
     * @param entity The entity to check for collisions.
     */
    public void checkTileCollision(Entity entity)
    {
        //Reset the entity's collision state
        entity.setCollision(false);

        // Calculate the grid column and row indices for the entity's current hitbox position
        int entityLeftCol = entity.getHitboxX() / gp.tileSize;
        int entityRightCol = (entity.getHitboxX() + entity.getHitboxWidth()) / gp.tileSize;
        int entityTopRow = entity.getHitboxY() / gp.tileSize;
        int entityBottomRow = (entity.getHitboxY() + entity.getHitboxHeight()) / gp.tileSize;

        int tileNum1, tileNum2;

        //Handle collision checks based on the entity's movement direction.
        switch (entity.getDirection())
        {
            case Direction.UP:
                //Calculate the top row the entity will occupy after moving up.
                entityTopRow = (entity.getHitboxY() - entity.getSpeed()) / gp.tileSize;

                //Get the tile indices for the top-left and top-right corners of the hitbox.
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];

                //If the entity is perfectly aligned on X-axis, check only the tile above
                if(entity.getWorldX() % gp.tileSize == 0)
                {
                    tileNum2 = tileNum1;
                }
                //Otherwise, check the top-left and top-right tiles
                else
                {
                    tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                }

                //If either tile is solid, mark the entity as colliding.
                if (gp.tileManager.tiles[tileNum1].isCollision() || gp.tileManager.tiles[tileNum2].isCollision()) {
                    entity.setCollision(true);
                }
                break;

            case Direction.DOWN:
                //Calculate the bottom row the entity will occupy after moving down.
                entityBottomRow = (entity.getHitboxY() + entity.getHitboxHeight() + entity.getSpeed() - 1) / gp.tileSize;

                //Get the tile indices for the bottom-left and bottom-right corners of the hitbox.
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];

                //If the entity is perfectly aligned on the X-axis, check only the tile below
                if(entity.getWorldX() % gp.tileSize == 0)
                {
                    tileNum2 = tileNum1;
                }
                //Otherwise, check the bottom-left and bottom-right tiles
                else
                {
                    tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                }

                //If either tile is solid, mark the entity as colliding.
                if (gp.tileManager.tiles[tileNum1].isCollision() || gp.tileManager.tiles[tileNum2].isCollision()) {
                    entity.setCollision(true);
                }
                break;

            case Direction.LEFT:
                //Calculate the left column the entity will occupy after moving left.
                entityLeftCol = (entity.getHitboxX() - entity.getSpeed()) / gp.tileSize;

                //Get the tile indices for the top-left and bottom-left corners of the hitbox.
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];

                //If the entity is perfectly aligned on the Y-axis, check only the tile to the left
                if(entity.getWorldY() % gp.tileSize == 0)
                {
                    tileNum2 = tileNum1;
                }
                //Otherwise, check the top-left and bottom-left tiles
                else
                {
                    tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                }

                //If either tile is solid, mark the entity as colliding.
                if (gp.tileManager.tiles[tileNum1].isCollision() || gp.tileManager.tiles[tileNum2].isCollision()) {
                    entity.setCollision(true);
                }
                break;

            case Direction.RIGHT:
                //Calculate the right column the entity will occupy after moving right.
                entityRightCol = (entity.getHitboxX() + entity.getHitboxWidth() + entity.getSpeed() - 1) / gp.tileSize;

                //Get the tile indices for the top-right and bottom-right corners of the hitbox.
                tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];

                //If the entity is perfectly aligned on the Y-axis, check only the tile to the right
                if(entity.getWorldY() % gp.tileSize == 0)
                {
                    tileNum2 = tileNum1;
                }
                //Otherwise, check the top-right and bottom-right tiles
                else
                {
                    tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                }

                //If either tile is solid, mark the entity as colliding.
                if (gp.tileManager.tiles[tileNum1].isCollision() || gp.tileManager.tiles[tileNum2].isCollision()) {
                    entity.setCollision(true);
                }
                break;
        }
    }

    /**
     * Checks for collisions between the player and the objects in the provided array.
     * If a collision is detected:
     * <ul>
     * <li>The player's score is updated based on the points of the collided object.</li>
     * <li>If the object is a Javacgum or SuperJavacgum:</li>
     *   <ol>
     *      <li>Increments the count of collected Javacgums.</li>
     *      <li>Removes the object (sets it to null).</li>
     *   </ol>
     * <li>For other objects:</li>
     *   <ol>
     *      <li>Marks the object to display its point value.</li>
     *      <li>Disables its hitbox by setting it to a zero-sized rectangle.</li>
     *   </ol>
     * </ul>
     *
     * @param player  The player object whose hitbox is checked for collisions.
     * @param objects The array of objects to check for collisions with the player.
     */
    public void checkObjectCollision(Player player, Object[] objects)
    {
        for(int i = 0; i < objects.length; i++)
        {
            if(objects[i] != null && player.hitbox.intersects(objects[i].hitbox) && !gp.restart)
            {
                gp.player.setScore(gp.player.getScore() + objects[i].getPoint());

                if(objects[i] instanceof Javacgum || objects[i] instanceof SuperJavacgum)
                {
                    gp.javacgumCollected++;
                    if(objects[i] instanceof SuperJavacgum)
                    {
                        if(gp.redGhost.getMode() != Mode.EATEN && gp.redGhost.hasSpawn()) { gp.redGhost.setMode(Mode.FRIGHTENED); }
                        if(gp.blueGhost.getMode() != Mode.EATEN && gp.blueGhost.hasSpawn()) { gp.blueGhost.setMode(Mode.FRIGHTENED); }
                        if(gp.pinkGhost.getMode() != Mode.EATEN && gp.pinkGhost.hasSpawn()) { gp.pinkGhost.setMode(Mode.FRIGHTENED); }
                        if(gp.orangeGhost.getMode() != Mode.EATEN && gp.orangeGhost.hasSpawn()) { gp.orangeGhost.setMode(Mode.FRIGHTENED); }
                    }
                    objects[i] = null;
                }
                else
                {
                    gp.collectedItems.add(objects[i].getName());
                    objects[i].setDisplayPoint(true);
                    //Disable hitbox
                    objects[i].hitbox = new Rectangle(0, 0 , 0 , 0);
                }
            }
        }
    }

    /**
     * Checks for a collision between the player and a ghost entity.
     * If a collision is detected, the player's life is reduced, and all entities are reset to their starting positions.
     *
     * @param player The player character whose collision with the ghost is being checked.
     * @param ghost The ghost entity to check for collision with the player.
     */
    public void checkEntityCollision(Player player, Entity ghost)
    {
        if(ghost.getMode() == Mode.SCATTER || ghost.getMode() == Mode.CHASE)
        {
            //Check if the player's hitbox intersects with the ghost's hitbox.
            if(player.hitbox.intersects(ghost.hitbox))
            {
                //Reduce the player's life by one when a collision occurs.
                gp.player.setLife(gp.player.getLife() - 1);

                if(gp.player.getLife() == 0)
                {
                    gp.state = gp.gameOverState;
                }
                else
                {
                    //Reset the player's position to their starting position.
                    gp.player.setStartPosition();

                    //Reset the positions of all ghosts to their starting positions.
                    gp.redGhost.setStartPosition();
                    gp.blueGhost.setStartPosition();
                    gp.pinkGhost.setStartPosition();
                    gp.orangeGhost.setStartPosition();

                    //Load the correct images if necessary
                    if(gp.redGhost.getMode() == Mode.FRIGHTENED || gp.redGhost.getMode() == Mode.EATEN)
                    {
                        gp.redGhost.getImage();
                    }
                    if(gp.blueGhost.getMode() == Mode.FRIGHTENED || gp.blueGhost.getMode() == Mode.EATEN)
                    {
                        gp.blueGhost.getImage();
                    }
                    if(gp.pinkGhost.getMode() == Mode.FRIGHTENED || gp.pinkGhost.getMode() == Mode.EATEN)
                    {
                        gp.pinkGhost.getImage();
                    }
                    if(gp.orangeGhost.getMode() == Mode.FRIGHTENED || gp.orangeGhost.getMode() == Mode.EATEN)
                    {
                        gp.orangeGhost.getImage();
                    }

                    //Reset the mode of all ghosts
                    gp.redGhost.setMode(Mode.CHASE);
                    gp.blueGhost.setMode(Mode.CHASE);
                    gp.pinkGhost.setMode(Mode.CHASE);
                    gp.orangeGhost.setMode(Mode.CHASE);

                    //Reset mode alternation
                    gp.redGhost.resetAlternation();
                    gp.blueGhost.resetAlternation();
                    gp.pinkGhost.resetAlternation();
                    gp.orangeGhost.resetAlternation();

                    //Reset counters
                    gp.blueGhost.resetSpawnProperties();
                    gp.pinkGhost.resetSpawnProperties();
                    gp.orangeGhost.resetSpawnProperties();

                    gp.redGhost.resetFrightenedCounter();
                    gp.blueGhost.resetFrightenedCounter();
                    gp.pinkGhost.resetFrightenedCounter();
                    gp.orangeGhost.resetFrightenedCounter();
                }
            }
        }
        else if(ghost.getMode() == Mode.FRIGHTENED && player.hitbox.intersects(ghost.hitbox))
        {
            //Update ghost's properties
            ghost.setMode(Mode.EATEN);
            ghost.setDisplayPointsWon(true);
            ghost.setWorldXDead(ghost.worldX);
            ghost.setWorldYDead(ghost.worldY);
            ghost.resetFrightenedCounter();
            Entity.GHOSTS_EATEN_IN_A_ROW++;

            //Adding points to player's score
            gp.player.setScore(gp.player.getScore() + Entity.calculatePointsWon());
        }
    }
}
