package io.github.killiansra.javacman.object;

import io.github.killiansra.javacman.main.GamePanel;

import java.awt.*;

public class Coffee extends Object
{
    public static final String name = "Coffee";

    public Coffee(GamePanel gp, int x, int y, int arrayIndex)
    {
        super(gp, 500, true);
        super.worldX = x;
        super.worldY = y;
        super.arrayIndex = arrayIndex;

        //Hitbox settings
        collisionHitbox = new Rectangle();
        collisionHitbox.x = worldX + 2;
        collisionHitbox.y = worldY;
        collisionHitbox.width = gp.tileSize - 4;
        collisionHitbox.height = gp.tileSize;

        getImage();
    }

    public Coffee(GamePanel gp)
    {
        super(gp, 0, false);

        getImage();
    }

    @Override
    public void getImage()
    {
        image = setup("collectible/coffee", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
