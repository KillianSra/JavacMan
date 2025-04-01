package main;

import ai.Pathfinder;
import storage.highscore.HighscoreManager;
import entity.*;
import object.Object;
import storage.settings.Settings;
import storage.settings.SettingsManager;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable
{
    //Screen settings
    private final int originalTileSize = 16;    //16 pixels
    private final float scale = 1.5F;
    public final int tileSize = (int) (originalTileSize * scale);   //24x24 pixels = 1 tile
    public final int maxScreenCol = 29;
    public final int maxScreenRow = 28;
    public int screenHeight = tileSize * maxScreenRow;    //672 pixels
    public int screenWidth = tileSize * maxScreenCol;     //696 pixels
    public final int originalScreenHeight = screenHeight;
    public final int originalScreenWidth = screenWidth;
    public final int minCol = 4;
    public final int minRow = 4;
    public final int maxCol = 24;
    public final int maxRow = 23;

    //Full screen settings
    public boolean fullScreen;
    public int screenHeightFS;
    public int screenWidthFS;
    public BufferedImage tempScreen;
    private Graphics2D tempGraphics;

    //Attribute
    private boolean firstIteration = true;
    public int highestScore;
    public boolean displayFPSCounter;
    private int FPS;

    //System
    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionManager collisionManager = new CollisionManager(this);
    public EventManager eventManager = new EventManager(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public Pathfinder pathfinder = new Pathfinder(this);
    public Sound sound = new Sound();
    Thread gameThread;

    //State
    public int state;
    public int previousState;

    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 3;
    public final int settingsState = 4;
    public final int controlsState = 5;
    public final int readyState = 6;

    //Counter
    private final int readyStateTime = 120;     //2 seconds
    public int readyCounter = 0;

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

        //Full screen settings
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        tempGraphics = tempScreen.createGraphics();

        //Create the highestScore.dat file if necessary
        HighscoreManager.initializeStorage();
        this.highestScore = HighscoreManager.load();
        setupGame();

        //Create the settings.dat file if necessary
        SettingsManager.initializeSettings();

        //Load the saved settings
        Settings settings = SettingsManager.load();
        this.fullScreen = settings.fullScreen();
        this.displayFPSCounter = settings.displayFPSCounter();
        this.sound.volumeScale = settings.volumeScale();
    }

    /**
     * Checks if fullscreen mode is enabled and applies it accordingly.
     * If fullscreen is active, it switches to fullscreen mode and updates window dimensions.
     *
     * <p>
     *     This method switches the window to fullscreen when starting the game if fullScreen = true is specified in
     *     settings.dat.
     * </p>
     */
    private void checkFullScreen()
    {
        if(fullScreen)
        {
            Main.setFullScreen();
            defineWindowDimension();
        }
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

    /**
     * Defines the game window dimensions based on the current screen mode.
     */
    public void defineWindowDimension()
    {
        if(fullScreen)
        {
            screenHeight = screenHeightFS;
            screenWidth = screenWidthFS;
        }
        else
        {
            screenHeight = tileSize * maxScreenRow;
            screenWidth = tileSize * maxScreenCol;
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
                //Check if the game should be in full screen when launched
                if(firstIteration)
                {
                    checkFullScreen();
                    firstIteration = false;
                }

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
                this.FPS = (int) drawCount;
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
        if(this.state == this.readyState)
        {
            if(++readyCounter == readyStateTime)
            {
                this.state = this.playState;
                readyCounter = 0;
            }
        }
        else if(this.state == this.playState)
        {
            //If the round is finished, go to the next one
            if(javacgumCollected == JAVACGUMS_IN_LEVELS)
            {
                setupGame();
                player.setStartPosition();

                //Reset ghosts state
                redGhost.reset();
                pinkGhost.reset();
                blueGhost.reset();
                orangeGhost.reset();

                keyHandler.reset();
                javacgumCollected = 0;
                nbSpecialCollectible = 2;
                currentRound++;

                state = readyState;
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

    /**
     * Saves the player's score if it is higher than the current highest score.
     */
    public void saveScore()
    {
        int score = player.getScore();

        if(score > highestScore)
        {
            HighscoreManager.save(score);
            highestScore = score;
        }
    }

    /**
     * Plays a sound effect from the specified index.
     *
     * <p>This method loads an audio file from the given index in the {@code sound} object
     * and plays it immediately.</p>
     *
     * @param i the index of the sound effect in the {@code sound} object.
     */
    public void playSoundEffect(int i)
    {
        sound.setFile(i);
        if(sound.volumeScale != 0)
        {
            sound.play();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Draw the game in a temp BufferedImage
        drawGame(tempGraphics);

        int offsetX = 0;
        //We check if the full screen window has finished building with the second condition
        if(fullScreen && Main.DEVICE.getFullScreenWindow() != null)
        {
            //Calculate x-offset to center game in fullscreen mode
            offsetX = (Main.DEVICE.getFullScreenWindow().getWidth() - Main.DEVICE.getFullScreenWindow().getHeight()) / 2 + tileSize;
        }

        //Draw the game
        g2.drawImage(tempScreen, offsetX, 0, screenWidth, screenHeight, null);

        g2.dispose();
    }

    /**
     * Renders the game elements onto the provided Graphics2D context.
     * <p>
     * This method is responsible for drawing all the game components, including the tile map,
     * objects, entities (player, ghosts), and the UI.
     * </p>
     *
     * @param g2 The {@code Graphics2D} instance used for rendering.
     */
    private void drawGame(Graphics2D g2)
    {
        //Reset the tempScreen
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        if(state == playState || state == pauseState || state == gameOverState || state == readyState)
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

        //Draw FPS counter
        if(displayFPSCounter)
        {
            //Convert long to String
            String fps = String.valueOf(this.FPS);

            //Draw the fps counter
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN,20f));
            g2.setColor(new Color(0, 255, 0));
            g2.drawString(fps, (maxScreenRow - 1) * tileSize, (maxScreenCol - 2) * tileSize);

            //Back to the previous g2 settings
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
            g2.setColor(Color.WHITE);
        }

        //DEBUG
        if(isDebuggingEnabled)
        {
            //Draw the triggers event area
            this.eventManager.draw(g2);
        }
    }
}