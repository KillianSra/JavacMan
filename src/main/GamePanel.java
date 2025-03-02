package main;

import ai.Pathfinder;
import entity.*;
import object.Object;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    public final int minCol = 4;
    public final int minRow = 4;
    public final int maxCol = 24;
    public final int maxRow = 23;

    //System
    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionManager collisionManager = new CollisionManager(this);
    public EventManager eventManager = new EventManager(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public Pathfinder pathfinder = new Pathfinder(this);
    Thread gameThread;

    //State
    public int state;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;

    //Game state
    private final int JAVACGUMS_IN_LEVELS = 167;
    public int currentRound = 1;
    public int javacgumCollected;
    public int nbSpecialCollectible;
    public ArrayList<String> collectedItems = new ArrayList<>();
    public boolean restart;

    //Entity
    public Player player = new Player(this, keyHandler);
    PlayerAvatar playerAvatar = new PlayerAvatar(this);
    public RedGhost redGhost = new RedGhost(this);
    public BlueGhost blueGhost = new BlueGhost(this);
    public PinkGhost pinkGhost = new PinkGhost(this);
    public OrangeGhost orangeGhost = new OrangeGhost(this);

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
        this.state = this.titleState;

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
    public void setupGame()
    {
        assetSetter.setObjects();
        javacgumCollected = 0;
        nbSpecialCollectible = 2;
        if(restart)
        {
            collectedItems.clear();
            currentRound = 1;
        }
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
                //System.out.println("FPS: " + drawCount);
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
        if(this.state == this.playState)
        {
            //If the round is finished, go to the next one
            if(javacgumCollected == JAVACGUMS_IN_LEVELS)
            {
                setupGame();
                player.setStartPosition();

                //Reset ghosts state
                redGhost.setStartPosition();
                redGhost.resetAlternation();

                blueGhost.setStartPosition();
                blueGhost.resetAlternation();
                blueGhost.resetSpawnProperties();

                pinkGhost.setStartPosition();
                pinkGhost.resetAlternation();
                pinkGhost.resetSpawnProperties();

                orangeGhost.setStartPosition();
                orangeGhost.resetAlternation();
                pinkGhost.resetSpawnProperties();

                keyHandler.reset();
                javacgumCollected = 0;
                nbSpecialCollectible = 2;
                currentRound++;
            }
            else
            {
                redGhost.update();
                blueGhost.update();
                pinkGhost.update();
                orangeGhost.update();
                player.update();

                //Handle special collectibles spawn
                if((javacgumCollected == 48 && nbSpecialCollectible == 2) || (javacgumCollected == 116 && nbSpecialCollectible == 1))
                {
                    assetSetter.setSpecialCollectible();
                    nbSpecialCollectible--;
                }

                //Manage special collectibles:
                //1. Check if the object has a limited life span and update it using `checkLifeSpan()`.
                //2. If the object is displaying points, manage its display duration with `checkDisplayedPoint()`.
                //3. Remove the object if it is marked for deletion.
                for(int i = 0; i < objects.length; i++)
                {
                    if(objects[i] != null)
                    {
                        if(objects[i].getDelete())
                        {
                            objects[i] = null;
                        }
                        else if(objects[i].getHasLimitedLifeSpan() && !objects[i].getDisplayPoint())
                        {
                            objects[i].checkLifeSpan(i);
                        }
                        else if(objects[i].getDisplayPoint())
                        {
                            objects[i].checkDisplayedPoint();
                        }
                    }
                }
            }
        }
        //Title screen animation
        else if(state == titleState)
        {
            playerAvatar.update();
        }

        if(restart)
        {
            setupGame();
            restart = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(state == playState || state == pauseState)
        {
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

            //Draw ghosts
            this.redGhost.draw(g2);
            this.blueGhost.draw(g2);
            this.pinkGhost.draw(g2);
            this.orangeGhost.draw(g2);

            //Draw the player
            this.player.draw(g2);
        }

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
