package object;

import main.GamePanel;

import java.awt.*;

public class Coffee extends Object
{
    public Coffee(GamePanel gp, int x, int y, int arrayIndex)
    {
        super(gp, 100, true);
        super.worldX = x;
        super.worldY = y;
        super.arrayIndex = arrayIndex;

        //Hitbox settings
        hitbox = new Rectangle();
        hitbox.x = worldX + 2;
        hitbox.y = worldY;
        hitbox.width = gp.tileSize - 4;
        hitbox.height = gp.tileSize;

        getImage();
    }

    @Override
    protected void getImage()
    {
        image = setup("collectible/coffee", gp.tileSize, gp.tileSize);
    }
}
