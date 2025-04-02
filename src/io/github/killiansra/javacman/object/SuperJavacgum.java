package io.github.killiansra.javacman.object;

import io.github.killiansra.javacman.main.GamePanel;

import java.awt.*;

public class SuperJavacgum extends Object
{
    public static final String name = "Super Javacgum";

    public SuperJavacgum(GamePanel gp, int x, int y)
    {
        super(gp, 50, false);
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

    //Method
    @Override
    public void getImage()
    {
        image = setup("collectible/super_javacgum", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
