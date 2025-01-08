package main;

import entity.Player;
import object.Object;
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
    public CollisionManager collisionManager = new CollisionManager(this);
    public EventManager eventManager = new EventManager(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    Thread gameThread;

    //Game state
    public int currentRound = 1;
    public int javacgumCollected = 0;
    public int nbSpecialCollectible = 2;

    //Entity
    public Player player = new Player(this, keyHandler);

    //Object
    public Object[] objects = new Object[167];

    //UI
    public UI UI = new UI(this);

    //Debug
    public final boolean isDebuggingEnabled = false;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        setupGame();
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

    /**
     * Configures the initial game state by setting up assets.
     */
    private void setupGame()
    {
        assetSetter.setObjects();
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
        player.update();

        //Handle special collectibles spawn
        if((javacgumCollected == 48 && nbSpecialCollectible == 2) || (javacgumCollected == 116 && nbSpecialCollectible == 1))
        {
            assetSetter.setSpecialCollectible();
            nbSpecialCollectible--;
        }

        //Handle special collectible life span
        for(int i = 0; i < objects.length; i++)
        {
            if(objects[i] != null && objects[i].getHasLimitedLifeSpan())
            {
                objects[i].checkLifeSpan(i);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Draw the game board
        this.tileManager.draw(g2);

        //Draw objects
        for (Object object : objects)
        {
            if (object != null)
            {
                object.draw(g2);
            }
        }

        //Draw the player
        this.player.draw(g2);

        //Draw UI
        this.UI.draw(g2);

        //DEBUG
        if(isDebuggingEnabled)
        {
            //Draw the triggers event area
            this.eventManager.draw(g2);
        }

        g2.dispose();
    }
}
