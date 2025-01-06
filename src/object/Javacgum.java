package object;

import main.GamePanel;

public class Javacgum extends Object
{
    public Javacgum(GamePanel gp, int x, int y)
    {
        super(gp);
        super.worldX = x;
        super.worldY = y;

        getImage();
    }

    @Override
    protected void getImage()
    {
        image = setup("collectible/javacgum", gp.tileSize, gp.tileSize);
    }
}