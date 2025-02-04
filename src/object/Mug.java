package object;

import main.GamePanel;

import java.awt.*;

public class Mug extends Object
{
    public static final String name = "Mug";

    public Mug(GamePanel gp, int x, int y, int arrayIndex)
    {
        super(gp, 300, true);
        super.worldX = x;
        super.worldY = y;
        super.arrayIndex = arrayIndex;

        //Hitbox settings
        hitbox = new Rectangle();
        hitbox.x = worldX + 2;
        hitbox.y = worldY + 4;
        hitbox.width = gp.tileSize - 6;
        hitbox.height = gp.tileSize - 8;

        getImage();
    }

    public Mug(GamePanel gp)
    {
        super(gp, 0, false);

        getImage();
    }

    @Override
    public void getImage()
    {
        image = setup("collectible/mug", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
