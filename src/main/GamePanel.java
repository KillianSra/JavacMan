package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable
{
    //Screen settings
    private final int originalTileSize = 16;    //16 pixels
    private final float scale = 1.5F;
    public final int tileSize = (int) (originalTileSize * scale);   //24x24 pixels = 1 tile
    public final int maxScreenCol = 30;
    public final int maxScreenRow = 26;
    public final int screenHeight = tileSize * maxScreenCol;    //720 pixels
    public final int screenWidth = tileSize * maxScreenRow;     //624 pixels

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        System.out.println(tileSize);
    }

    @Override
    public void run()
    {

    }
}
