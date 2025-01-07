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

    //Setters
    public void setDirection(Direction direction) { this.direction = direction; }

    //Abstract method
    /**
     * Updates the state or behavior of the entity.
     */
    public abstract void update();

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
}