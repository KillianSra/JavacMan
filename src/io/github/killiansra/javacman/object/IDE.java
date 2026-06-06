package io.github.killiansra.javacman.object;

import io.github.killiansra.javacman.main.GamePanel;

import java.awt.*;

public class IDE extends Object
{
    public static final String name = "IDE";

    public IDE(GamePanel gp, int x, int y, int arrayIndex)
    {
        super(gp, 1000, true);
        super.worldX = x;
        super.worldY = y;
        super.arrayIndex = arrayIndex;

        //Hitbox settings
        collisionHitbox = new Rectangle();
        collisionHitbox.x = worldX;
        collisionHitbox.y = worldY;
        collisionHitbox.width = gp.tileSize;
        collisionHitbox.height = gp.tileSize;

        getImage();
    }

    public IDE(GamePanel gp)
    {
        super(gp, 0, false);

        getImage();
    }

    @Override
    public void getImage()
    {
        image = setup("collectible/eclipse", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
