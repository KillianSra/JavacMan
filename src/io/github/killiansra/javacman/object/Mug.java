package io.github.killiansra.javacman.object;

import io.github.killiansra.javacman.main.GamePanel;

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
        collisionHitbox = new Rectangle();
        collisionHitbox.x = worldX + 2;
        collisionHitbox.y = worldY + 4;
        collisionHitbox.width = gp.tileSize - 6;
        collisionHitbox.height = gp.tileSize - 8;

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
