package io.github.killiansra.javacman.object;

import io.github.killiansra.javacman.main.GamePanel;

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

    public Lightbulb(GamePanel gp)
    {
        super(gp, 0, false);

        getImage();
    }

    @Override
    public void getImage()
    {
        image = setup("collectible/lightbulb", gp.tileSize, gp.tileSize);
    }

    @Override
    public String getName()
    {
        return name;
    }
}
