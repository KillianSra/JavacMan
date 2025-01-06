package object;

import main.GamePanel;

public class SuperJavacgum extends Object
{
    public SuperJavacgum(GamePanel gp, int x, int y)
    {
        super(gp);
        super.worldX = x;
        super.worldY = y;

        getImage();
    }

    @Override
    protected void getImage()
    {
        image = setup("collectible/super_javacgum", gp.tileSize, gp.tileSize);
    }
}
