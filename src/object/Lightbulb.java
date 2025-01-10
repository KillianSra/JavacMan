package object;

import main.GamePanel;

import java.awt.*;

public class Lightbulb extends Object
{
    public static final String name = "Lightbulb";

    public Lightbulb(GamePanel gp, int x, int y, int arrayIndex)
    {
        super(gp, 2000, true);
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
        image = setup("collectible/lightbulb", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
