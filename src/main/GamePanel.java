package main;

import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable
{
    //Screen settings
    private final int originalTileSize = 16;    //16 pixels
    private final float scale = 1.5F;
    public final int tileSize = (int) (originalTileSize * scale);   //24x24 pixels = 1 tile
    public final int maxScreenCol = 29;
    public final int maxScreenRow = 28;
    public final int screenHeight = tileSize * maxScreenRow;    //720 pixels
    public final int screenWidth = tileSize * maxScreenCol;     //624 pixels

    //System
    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;

    //TODO: delete this after player class implementation
    int playerX = this.tileSize * 4;
    int playerY = this.tileSize * 5;
    int playerSpeed = 3;


    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);
    }

    /**
     * Starts the game thread for the main game loop.
     */
    public void startGameThread()
    {
        this.gameThread = new Thread(this);

        //automatically call run() method
        this.gameThread.start();
    }

    @Override
    public void run()
    {
        //FPS
        int FPS = 60;
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;

        //Game loop : runs continuously as long as the game thread is active
        while(gameThread != null)
        {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            //If enough time has passed to process a frame
            if(delta >= 1)
            {
                //Update game logic
                update();

                //Render the updated game state. Automatically call paintComponent() method
                repaint();

                //Decrease delta to account for the processed frame
                delta--;
                drawCount++;
            }

            //DEBUG
            if(timer >= 1000000000)
            {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    /**
     * Update the game logic.
     */
    private void update()
    {
        if(keyHandler.upPressed)
        {
            playerY -= playerSpeed;
        }
        else if(keyHandler.downPressed)
        {
            playerY += playerSpeed;
        }
        else if(keyHandler.leftPressed)
        {
            playerX -= playerSpeed;
        }
        else if(keyHandler.rightPressed)
        {
            playerX += playerSpeed;
        }

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Draw the game board
        this.tileManager.draw(g2);

        //TODO : delete this after player class implementation
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, tileSize, tileSize);

        g2.dispose();
    }
}
