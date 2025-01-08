package object;

import main.GamePanel;

public class Coffee extends Object
{
    public Coffee(GamePanel gp, int x, int y)
    {
        super(gp, 100);
        super.worldX = x;
        super.worldY = y;

        getImage();
    }

    @Override
    protected void getImage()
    {
        image = setup("collectible/coffee", gp.tileSize, gp.tileSize);
    }
}
