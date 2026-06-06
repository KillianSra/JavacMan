package io.github.killiansra.javacman.entity.interfaces;

public interface Ghost
{
    /**
     * Executes the scatter mode behavior for the javacman.entity.
     * In scatter mode, the entity follows a predefined loop by sequentially updating its direction
     * and position based on specific waypoints on the map.
     * Each scatter phase corresponds to a segment of the loop, and the entity transitions
     * to the next phase upon reaching the designated waypoint.
     */
    void scatterMode();

    /**
     * Executes the pathfinding logic for the ghost entity.
     */
    void pathfinding();
}
