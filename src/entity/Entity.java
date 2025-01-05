package entity;

import main.GamePanel;
import main.Tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Entity
{
    protected GamePanel gp;
    protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    protected Rectangle hitbox;
    public int hitboxDefaultX;
    public int hitboxDefaultY;

    //State
    protected int worldX, worldY;
    protected Direction direction = Direction.DOWN;
    private boolean collision;
    protected int spriteNum = 1;

    //Counter
    protected int spriteCounter;

    //Attribute
    public int speed;
    public int defaultSpeed;

    public Entity(GamePanel gp)
    {
        this.gp = gp;
    }

    //Getters
    public int getWorldX() { return this.worldX; }
    public int getWorldY() { return this.worldY; }
    public int getSpeed() { return this.speed; }
    public int getHitboxX() { return this.hitbox.x; }
    public int getHitboxY() { return this.hitbox.y; }
    public int getHitboxHeight() { return this.hitbox.height; }
    public int getHitboxWidth() { return this.hitbox.width; }
    public Direction getDirection() { return this.direction; }
    public boolean isCollision() { return this.collision; }

    //Setters
    public void setWorldX(int worldX) { this.worldX = worldX; }
    public void setWorldY(int worldY) { this.worldY = worldY; }
    public void setDirection(Direction direction) { this.direction = direction; }
    public void setCollision(boolean collision) { this.collision = collision; }

    //Abstract methods
    /**
     * Updates the state or behavior of the entity.
     */
    public abstract void update();

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

    //Methods
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