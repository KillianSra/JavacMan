package main;

import object.Javacgum;
import object.SuperJavacgum;

public class AssetSetter
{
    GamePanel gp;

    public AssetSetter(GamePanel gp)
    {
        this.gp = gp;
    }

    /**
     * Initializes and places game objects on the game board.
     */
    public void setObjects()
    {

        int index = 0;

        // Row 1
        for (int i = 4; i < 25; i++)
        {
            if (i != 9 && i != 19)
            {
                gp.objects[index++] = new Javacgum(gp, gp.tileSize * i, gp.tileSize * 4);
            }
        }

        // Row 2
        gp.objects[index++] = new SuperJavacgum(gp, gp.tileSize * 4, gp.tileSize * 5);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 8, gp.tileSize * 5);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 10, gp.tileSize * 5);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 18, gp.tileSize * 5);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 20, gp.tileSize * 5);
        gp.objects[index++] = new SuperJavacgum(gp, gp.tileSize * 24, gp.tileSize * 5);

        // Row 3
        for (int i = 4; i < 25; i++)
        {
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * i, gp.tileSize * 6);
        }

        // Row 4 and 5
        for (int i = 7; i < 9; i++)
        {
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * 6, gp.tileSize * i);
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * 8, gp.tileSize * i);
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * 13, gp.tileSize * i);
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * 15, gp.tileSize * i);
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * 20, gp.tileSize * i);
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * 22, gp.tileSize * i);
        }

        // Row 6
        for (int i = 6; i < 23; i++)
        {
            if (i != 7 && i != 14 && i != 21)
            {
                gp.objects[index++] = new Javacgum(gp, gp.tileSize * i, gp.tileSize * 9);
            }
        }

        // Row 7 to 13
        for (int i = 10; i < 17; i++)
        {
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * 6, gp.tileSize * i);
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * 22, gp.tileSize * i);
        }

        // Row 14
        for (int i = 6; i < 23; i++)
        {
            if (i <= 11 || i >= 17)
            {
                gp.objects[index++] = new Javacgum(gp, gp.tileSize * i, gp.tileSize * 17);
            }
        }

        // Row 15
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 6, gp.tileSize * 18);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 11, gp.tileSize * 18);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 17, gp.tileSize * 18);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 22, gp.tileSize * 18);

        // Row 16
        for (int i = 4; i < 25; i++)
        {
            if (i != 14)
            {
                gp.objects[index++] = new Javacgum(gp, gp.tileSize * i, gp.tileSize * 19);
            }
        }

        // Row 17
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 4, gp.tileSize * 20);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 8, gp.tileSize * 20);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 13, gp.tileSize * 20);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 15, gp.tileSize * 20);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 20, gp.tileSize * 20);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 24, gp.tileSize * 20);

        // Row 18
        gp.objects[index++] = new SuperJavacgum(gp, gp.tileSize * 4, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 8, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 10, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 11, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 12, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 13, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 15, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 16, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 17, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 18, gp.tileSize * 21);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 20, gp.tileSize * 21);
        gp.objects[index++] = new SuperJavacgum(gp, gp.tileSize * 24, gp.tileSize * 21);

        //Row 19
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 4, gp.tileSize * 22);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 8, gp.tileSize * 22);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 10, gp.tileSize * 22);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 18, gp.tileSize * 22);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 20, gp.tileSize * 22);
        gp.objects[index++] = new Javacgum(gp, gp.tileSize * 24, gp.tileSize * 22);

        //Row 20
        for(int i = 4; i < 25; i++)
        {
            gp.objects[index++] = new Javacgum(gp, gp.tileSize * i, gp.tileSize * 23);
        }
    }
}