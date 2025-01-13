package entity;

import main.GamePanel;
import main.Renderable;

import java.awt.image.BufferedImage;

public abstract class Entity extends Renderable
{
    protected GamePanel gp;
    protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public int hitboxDefaultX;
    public int hitboxDefaultY;

    //State
    protected Direction direction = Direction.LEFT;
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
    public int getSpeed() { return this.speed; }
    public Direction getDirection() { return this.direction; }
    public BufferedImage getLeft2() { return this.left2; }

    //Setters
    public void setDirection(Direction direction) { this.direction = direction; }

    //Abstract method
    /**
     * Updates the state or behavior of the entity.
     */
    public abstract void update();

    //Methods
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
}