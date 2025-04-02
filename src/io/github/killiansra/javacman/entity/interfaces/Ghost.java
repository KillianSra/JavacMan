package io.github.killiansra.javacman.entity.interfaces;

public interface Ghost
{
    /**
     * Executes the scatter mode behavior for the io.github.killiansra.javacman.entity.
     * In scatter mode, the io.github.killiansra.javacman.entity follows a predefined loop by sequentially updating its direction
     * and position based on specific waypoints on the map.
     * Each scatter phase corresponds to a segment of the loop, and the io.github.killiansra.javacman.entity transitions
     * to the next phase upon reaching the designated waypoint.
     */
    void scatterMode();

    /**
     * Executes the pathfinding logic for the ghost io.github.killiansra.javacman.entity.
     */
    void pathfinding();
}
