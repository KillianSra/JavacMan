package object;

import main.GamePanel;

import java.awt.*;

public class SuperJavacgum extends Object
{
    public SuperJavacgum(GamePanel gp, int x, int y)
    {
        super(gp);
        super.worldX = x;
        super.worldY = y;

        //Hitbox settings
        hitbox = new Rectangle();
        hitbox.x = worldX + (gp.tileSize / 2) - 4;
        hitbox.y = worldY + (gp.tileSize / 2) - 4;
        hitbox.width = 8;
        hitbox.height = 8;

        getImage();
    }

    @Override
    protected void getImage()
    {
        image = setup("collectible/super_javacgum", gp.tileSize, gp.tileSize);
    }
}
