package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Renderable
{
    protected Rectangle hitbox;
    public final int POINT_DISPLAYED_TIME = 60;     //1 second

    //State
    protected int worldX, worldY = -1;
    protected boolean collision;

    //Counter
    protected int displayPointsWonCounter = 0;

    //Getters
    public Rectangle getHitbox() { return this.hitbox; }
    public int getHitboxX() { return this.hitbox.x; }
    public int getHitboxY() { return this.hitbox.y; }
    public int getHitboxHeight() { return this.hitbox.height; }
    public int getHitboxWidth() { return this.hitbox.width; }
    public int getWorldX() { return this.worldX; }
    public int getWorldY() { return this.worldY; }
    public boolean isCollision() { return collision; }

    //Setters
    public void setWorldX(int worldX) { this.worldX = worldX; }
    public void setWorldY(int worldY) { this.worldY = worldY; }
    public void setCollision(boolean collision) { this.collision = collision; }


    //Abstract methods
    /**
     * Reads images and stores them in BufferedImage
     */
    protected abstract void getImage();

    /**
     * Renders the visual representation of the entity on the screen.
     *
     * @param g2 the Graphics2D object used for drawing.
     */
    public abstract void draw(Graphics2D g2);

    /**
     * Tracks the display duration of points on the screen.
     * Increments the counter until it reaches the predefined limit.
     */
    public abstract void checkDisplayedPoint();

    /**
     * Loads an image from the given path, scales it to the specified dimensions, and returns the resulting BufferedImage.
     *
     * @param imagePath The relative path to the image file (without the .png extension).
     * @param width The desired width of the scaled image.
     * @param height The desired height of the scaled image.
     * @return A BufferedImage object representing the scaled image.
     * @throws RuntimeException if an IOException occurs while reading the image file or if the file cannot be found.
     */
    protected BufferedImage setup(String imagePath, int width, int height)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath + ".png")));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return Tools.scaleImage(image, width, height);
    }
}