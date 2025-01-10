package object;

import main.GamePanel;

import java.awt.*;

public class Javacgum extends Object
{
    public static final String name = "Javacgum";

    public Javacgum(GamePanel gp, int x, int y)
    {
        super(gp, 10, false);
        super.worldX = x;
        super.worldY = y;

        //Hitbox settings
        hitbox = new Rectangle();
        hitbox.x = worldX + (gp.tileSize / 2) - 1;
        hitbox.y = worldY + (gp.tileSize / 2) - 1;
        hitbox.width = 2;
        hitbox.height = 2;

        getImage();
    }

    @Override
    protected void getImage()
    {
        image = setup("collectible/javacgum", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}