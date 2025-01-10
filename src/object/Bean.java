package object;

import main.GamePanel;

import java.awt.*;

public class Bean extends Object
{
    public static final String name = "Bean";

    public Bean(GamePanel gp, int x, int y, int arrayIndex)
    {
        super(gp, 100, true);
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

    @Override
    protected void getImage()
    {
        image = setup("collectible/bean", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
