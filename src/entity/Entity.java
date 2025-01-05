package entity;

import main.GamePanel;

import java.awt.*;

public abstract class Entity
{
    protected GamePanel gp;
    public Rectangle hitbox;
    public int hitboxDefaultX;
    public int hitboxDefaultY;

    //State
    protected int worldX, worldY;
    private Direction direction = Direction.DOWN;
    private boolean collision;

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
            case Direction.UP: setWorldY(getWorldY() - getSpeed()); break;
            case Direction.DOWN: setWorldY(getWorldY() + getSpeed()); break;
            case Direction.LEFT: setWorldX(getWorldX() - getSpeed()); break;
            case Direction.RIGHT: setWorldX(getWorldX() + getSpeed()); break;
        }

        //Synchronize hitbox with entity position
        hitbox.x = worldX;
        hitbox.y = worldY;
    }
}