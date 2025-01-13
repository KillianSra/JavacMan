package object;

import main.GamePanel;

import java.awt.*;

public class Computer extends Object
{
    public static final String name = "Computer";

    public Computer(GamePanel gp, int x, int y, int arrayIndex)
    {
        super(gp, 700, true);
        super.worldX = x;
        super.worldY = y;
        super.arrayIndex = arrayIndex;

        //Hitbox settings
        hitbox = new Rectangle();
        hitbox.x = worldX;
        hitbox.y = worldY;
        hitbox.width = gp.tileSize;
        hitbox.height = gp.tileSize;

        getImage();
    }

    public Computer(GamePanel gp)
    {
        super(gp, 0, false);

        getImage();
    }

    @Override
    protected void getImage()
    {
        image = setup("collectible/computer", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
