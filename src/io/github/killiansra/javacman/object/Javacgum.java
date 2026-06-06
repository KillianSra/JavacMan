package io.github.killiansra.javacman.object;

import io.github.killiansra.javacman.main.GamePanel;

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
        collisionHitbox = new Rectangle();
        collisionHitbox.x = worldX + (gp.tileSize / 2) - 1;
        collisionHitbox.y = worldY + (gp.tileSize / 2) - 1;
        collisionHitbox.width = 2;
        collisionHitbox.height = 2;

        getImage();
    }

    @Override
    public void getImage()
    {
        image = setup("collectible/javacgum", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}