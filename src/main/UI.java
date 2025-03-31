package main;

import entity.abstracts.Entity;
import object.Object;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI
{
    GamePanel gp;
    Font maruMonica;
    BufferedImage playerLife;

    //Selection menu
    final int pauseState = 1;
    int subState;
    public int commandNb;

    public boolean changeKey = false;

    public UI(GamePanel gp)
    {
        this.gp = gp;

        //Load custom font
        try
        {
            InputStream is = getClass().getClassLoader().getResourceAsStream("font/x12y16pxMaruMonica.ttf");
            this.maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch (IOException | FontFormatException e)
        {
            throw new RuntimeException(e);
        }

        //Initialize HUD objects
        playerLife = gp.player.getLeft2();
    }

    /**
     * Draws the "READY!" message on the screen before the game starts.
     *
     * @param g2 the {@link Graphics2D} object used for rendering the text
     */
    private void drawReady(Graphics2D g2)
    {
        String text = "READY !";
        int x = getXCentered(text, g2, 2);
        int y = gp.tileSize * 16;

        //Set text color to yellow and apply bold font with size 32
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));

        //Draw the text
        g2.drawString(text, x, y);

        //Reset Graphics2D
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25f));
    }

    /**
     * Draws the Heads-Up Display (HUD) on the screen.
     *
     * @param g2 Graphics2D instance used for rendering the HUD elements
     */
    private void drawHUD(Graphics2D g2)
    {
        //Score board
        int y = (int) (gp.tileSize * 1.5);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
        g2.drawString("1UP", getXCentered("1UP", g2, 4), y);
        g2.drawString("HIGH SCORE", getXCentered("HIGH SCORE", g2, 2), y);

        //Player's score
        y *= 2;
        String text = String.valueOf(gp.player.getScore());
        g2.drawString(text, getXCentered(text, g2, 4), y);

        //Highest score
        String highestScore = String.valueOf(gp.highestScore);
        g2.drawString(highestScore, getXCentered(highestScore, g2, 2) , y);

        //Player's life icons
        int x = gp.tileSize * 4;
        y = gp.tileSize * 25;

        for(int i = 1; i < gp.player.getLife(); i++)
        {
            g2.drawImage(this.playerLife, x, y, null);
            x += gp.tileSize + 5;
        }

        //Collectible items
        x = gp.tileSize * 23;
        int tempX = x;
        for(int i = 0; i < gp.collectedItems.size(); i++)
        {
            Object obj = Tools.getObjectByName(gp, gp.collectedItems.get(i));
            g2.drawImage(obj.getObjectImage(), tempX, y, null);
            tempX -= gp.tileSize - 2;
        }


        //Number of round done
        x += (int) (gp.tileSize * 1.3);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
        g2.drawString(String.valueOf(gp.currentRound), x, y + gp.tileSize);
    }

    /**
     * Sets a dark transparent background overlay on the screen.
     *
     * @param g2 Graphics2D instance used for rendering the background.
     */
    private void setDarkTransparentBackground(Graphics2D g2)
    {
        g2.setColor(new Color(0, 0, 0));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

    /**
     * Draws the pause menu on the screen.
     *
     * @param g2 Graphics2D instance used for rendering the pause menu
     */
    private void drawPauseMenu(Graphics2D g2)
    {
        //Darkened the screen
        setDarkTransparentBackground(g2);

        //Draw the "PAUSED" title at the center of the screen
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 65F));
        g2.drawString("PAUSED", getXCentered("PAUSED", g2, 2), gp.tileSize * 10);

        //Draw the "RESUME" option
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
        int x = getXCentered("RESUME", g2, 2);
        int y = gp.tileSize * 15;
        g2.drawString("RESUME",x , y);
        if(commandNb == 0)
        {
            g2.drawRoundRect(x - gp.tileSize * 2, (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }

        //Draw the "SETTINGS" option
        y += gp.tileSize * 3;
        g2.drawString("SETTINGS", getXCentered("SETTINGS", g2, 2), y);
        if(commandNb == 1)
        {
            g2.drawRoundRect(x - gp.tileSize * 2, (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }

        //Draw the "QUIT" option
        y += gp.tileSize * 3;
        g2.drawString("QUIT", getXCentered("QUIT", g2, 2), y);
        if(commandNb == 2)
        {
            g2.drawRoundRect(x - gp.tileSize * 2, (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }

        //Reset Graphics2D
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
    }

    /**
     * Draws the title screen of the game.
     *
     * @param g2 Graphics2D instance used for rendering the title screen elements
     */
    private void drawTitleScreen(Graphics2D g2)
    {
        //Draw the title text
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 65F));
        g2.drawString("JavacMan", getXCentered("JavacMan", g2, 2), gp.tileSize * 8);

        //Render the animation (player avatar moving on the title screen)
        gp.playerAvatar.draw(g2);

        //Draw the "PLAY" option
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
        int x = getXCentered("PLAY", g2, 2);
        int y = gp.tileSize * 17;
        g2.drawString("PLAY", x, y);
        if(commandNb == 0)
        {
            g2.drawRoundRect((int) (x - gp.tileSize * 2.75), (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }

        //Draw the "SETTINGS" option
        y += gp.tileSize * 3;
        g2.drawString("SETTINGS", getXCentered("SETTINGS", g2, 2), y);
        if(commandNb == 1)
        {
            g2.drawRoundRect((int) (x - gp.tileSize * 2.75), (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }

        //Draw the "QUIT" option
        y += gp.tileSize * 3;
        g2.drawString("QUIT", getXCentered("QUIT", g2, 2), y);
        if(commandNb == 2)
        {
            g2.drawRoundRect((int) (x - gp.tileSize * 2.75), (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }
    }

    /**
     * Draws the settings screen.
     *
     * @param g2 Graphics2D instance used for rendering the settings screen elements
     */
    private void drawSettingsScreen(Graphics2D g2)
    {
        int x, y;

        //Draw the settings text
        y = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 65F));
        g2.drawString("SETTINGS", getXCentered("SETTINGS", g2, 2), y);

        //Sound label
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x = gp.tileSize * 5;
        y += gp.tileSize * 5;
        g2.drawString("Sound effects : ", x, y);
        if(commandNb == 0)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //Sound volume bar
        x += gp.tileSize * 9;
        y -= gp.tileSize - 2;
        g2.drawRect(x, y, 240, 24);
        int volumeWidth = 48 * gp.sound.volumeScale;
        g2.fillRect(x, y, volumeWidth, 24);

        //Full screen
        x = gp.tileSize * 5;
        y = gp.tileSize * 11;
        g2.drawString("Full screen : ", x, y);
        if(commandNb == 1)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //Full screen checkbox
        x += gp.tileSize * 9;
        y -= gp.tileSize - 2;
        g2.drawRect(x, y, 24, 24);
        if(gp.fullScreen)
        {
            g2.fillRect(x, y, 24, 24);
        }

        //FPS counter
        x = gp.tileSize * 5;
        y = gp.tileSize * 13;
        g2.drawString("Display FPS : ", x, y);
        if(commandNb == 2)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //FPS counter checkbox
        x += gp.tileSize * 9;
        y -= gp.tileSize - 2;
        g2.drawRect(x, y, 24, 24);
        if(gp.displayFPSCounter)
        {
            g2.fillRect(x, y, 24, 24);
        }

        //Commands
        x = gp.tileSize * 5;
        y = gp.tileSize * 15;
        g2.drawString("Controls", x, y);
        if(commandNb == 3)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //Save button
        x = getXCentered("Save", g2, 2);
        y = gp.tileSize * 23;
        g2.setFont(g2.getFont().deriveFont(42f));
        g2.drawString("Save", x, y);
        g2.setFont(g2.getFont().deriveFont(32f));
        if(commandNb == 4)
        {
            g2.drawRoundRect((int) (x - gp.tileSize * 2.75), (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }

        //Command helper
        x = gp.tileSize * 2;
        y = gp.tileSize * 26 + 12;
        g2.drawString(gp.keyHandler.getKeyByKeycode(gp.keyHandler.pause) + " Back", x, y);
    }

    /**
     * Draws the controls screen.
     *
     * @param g2 Graphics2D instance used for rendering the controls screen elements
     */
    private void drawControlsScreen(Graphics2D g2)
    {
        int x, y;

        //Draw the controls text
        y = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 65F));
        g2.drawString("Controls", getXCentered("Controls", g2, 2), y);

        //Label
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x = gp.tileSize * 5;
        y += gp.tileSize * 5;
        g2.drawString("//IN GAME", x, y);

        //Up
        y += gp.tileSize * 2;
        g2.drawString("Up : ", x, y);
        g2.drawString(gp.keyHandler.getKeyByKeycode(gp.keyHandler.up) + " or [↑]", x + gp.tileSize * 4, y);
        if(commandNb == 0 && !changeKey)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //Down
        y += gp.tileSize * 2;
        g2.drawString("Down : ", x, y);
        g2.drawString(gp.keyHandler.getKeyByKeycode(gp.keyHandler.down) + " or [↓]", x + gp.tileSize * 4, y);
        if(commandNb == 1 && !changeKey)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //Left
        y += gp.tileSize * 2;
        g2.drawString("Left : ", x, y);
        g2.drawString(gp.keyHandler.getKeyByKeycode(gp.keyHandler.left) + " or [←]", x + gp.tileSize * 4, y);
        if(commandNb == 2)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //Right
        y += gp.tileSize * 2;
        g2.drawString("Right : ", x, y);
        g2.drawString(gp.keyHandler.getKeyByKeycode(gp.keyHandler.right) + " or [→]", x + gp.tileSize * 4, y);
        if(commandNb == 3)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //Pause
        y += gp.tileSize * 2;
        g2.drawString("Pause : ", x, y);
        g2.drawString(gp.keyHandler.getKeyByKeycode(gp.keyHandler.pause), x + gp.tileSize * 4, y);
        if(commandNb == 4)
        {
            g2.drawString(">", x - gp.tileSize, y);
        }

        //Save button
        x = getXCentered("Save", g2, 2);
        y = gp.tileSize * 23;
        g2.setFont(g2.getFont().deriveFont(42f));
        g2.drawString("Save", x, y);
        g2.setFont(g2.getFont().deriveFont(32f));
        if(commandNb == 5)
        {
            g2.drawRoundRect((int) (x - gp.tileSize * 2.75), (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }

        //Command helper
        x = gp.tileSize * 2;
        y = gp.tileSize * 26 + 12;
        g2.drawString(gp.keyHandler.getKeyByKeycode(gp.keyHandler.pause) + " Back", x, y);
    }

    /**
     * Draws the ket change window.
     *
     * @param g2 Graphics2D instance used for rendering the key change window elements
     */
    private void drawKeyChangeWindow(Graphics2D g2)
    {
        //Sub window construction
        int x = gp.tileSize * 9 + gp.tileSize / 2;
        int y = gp.tileSize * 9 + gp.tileSize / 2;

        //border
        g2.fillRoundRect(x, y, gp.tileSize * 10, gp.tileSize * 7, 30, 30);

        //Rectangle
        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(x + 2, y + 2, gp.tileSize * 10 - 4, gp.tileSize * 7 - 4, 30, 30);

        //Title
        g2.setColor(Color.WHITE);
        g2.drawString("Press a key", getXCentered("Press a key", g2, 2), (int) (y + gp.tileSize * 1.5));

        //Display the selected key
        if(gp.keyHandler.keyCode != -1)
        {
            g2.setFont(g2.getFont().deriveFont(24f));

            int keycode = gp.keyHandler.keyCode;
            String key = gp.keyHandler.getKeyByKeycode(keycode);
            int centerX = getXCentered(String.valueOf(key), g2, 2);
            g2.drawString(key, centerX, y + gp.tileSize * 4);

            //Command helper
            y += gp.tileSize * 6;
            g2.drawString("[Enter] Save", getXCentered("[Enter] Save", g2, 2), y);

            g2.setFont(g2.getFont().deriveFont(32f));
        }
    }

    /**
     * Draws the game over screen of the game.
     *
     * @param g2 Graphics2D instance used for rendering the game over screen elements
     */
    private void drawGameOverMenu(Graphics2D g2)
    {
        //Darkened the screen
        setDarkTransparentBackground(g2);

        //Draw the "GAME OVER" title at the center of the screen
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 65F));
        g2.drawString("GAME OVER", getXCentered("GAME OVER", g2, 2), gp.tileSize * 10);

        //Draw the "RETRY" option
        g2.setFont(g2.getFont().deriveFont(35F));
        int x = getXCentered("RETRY", g2, 2);
        int y = gp.tileSize * 15;
        g2.drawString("RETRY",x , y);
        if(commandNb == 0)
        {
            g2.drawRoundRect((int) (x - gp.tileSize * 2.5), (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }

        //Draw the "QUIT" option
        y += gp.tileSize * 3;
        g2.drawString("QUIT", getXCentered("QUIT", g2, 2), y);
        if(commandNb == 1)
        {
            g2.drawRoundRect((int) (x - gp.tileSize * 2.5), (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }
    }

    /**
     * Draws the points won when the player eats a ghost.
     *
     * @param g2 The Graphics2D object used to render the points on the screen.
     */
    private void drawPointsWon(Graphics2D g2)
    {
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12));

        FontMetrics fm = g2.getFontMetrics();
        String points = String.valueOf(Entity.calculatePointsWon());
        int textWidth = fm.stringWidth(points);
        int textHeight = fm.getHeight();

        int xCentered, yCentered;

        //Display points earned at the location where the player ate the ghost
        if(gp.redGhost.getDisplayPointsWon())
        {
            xCentered = gp.redGhost.getWorldXDead() + (gp.tileSize / 2) - (textWidth / 2);
            yCentered = gp.redGhost.getWorldYDead() - (textHeight / 2) + gp.tileSize;
            g2.drawString(points, xCentered, yCentered);
        }
        if(gp.blueGhost.getDisplayPointsWon())
        {
            xCentered = gp.blueGhost.getWorldXDead() + (gp.tileSize / 2) - (textWidth / 2);
            yCentered = gp.blueGhost.getWorldYDead() - (textHeight / 2) + gp.tileSize;
            g2.drawString(points, xCentered, yCentered);
        }
        if(gp.pinkGhost.getDisplayPointsWon())
        {
            xCentered = gp.pinkGhost.getWorldXDead() + (gp.tileSize / 2) - (textWidth / 2);
            yCentered = gp.pinkGhost.getWorldYDead() - (textHeight / 2) + gp.tileSize;
            g2.drawString(points, xCentered, yCentered);
        }
        if(gp.orangeGhost.getDisplayPointsWon())
        {
            xCentered = gp.orangeGhost.getWorldXDead() + (gp.tileSize / 2) - (textWidth / 2);
            yCentered = gp.orangeGhost.getWorldYDead() - (textHeight / 2) + gp.tileSize;
            g2.drawString(points, xCentered, yCentered);
        }
    }

    /**
     * Calculates the x-coordinate to center the given text on the screen based on the position factor.
     *
     * @param text The text to be centered
     * @param g2 Graphics2D instance used for text measurement
     * @param position A position factor that adjusts the centering
     * @return The x-coordinate where the text should be drawn
     */
    private int getXCentered(String text, Graphics2D g2, int position)
    {
        int width = gp.screenWidth;
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        if(gp.fullScreen)
        {
            width = gp.originalScreenWidth;
        }
        return width / position - length / position;
    }

    /**
     * Draws UI elements on the screen.
     *
     * @param g2 Graphics2D instance used for rendering the UI elements
     */
    public void draw(Graphics2D g2)
    {
        g2.setFont(maruMonica);
        g2.setColor(Color.WHITE);

        if(gp.state == gp.readyState)
        {
            drawHUD(g2);
            drawReady(g2);
        }
        else if(gp.state == gp.playState)
        {
            drawHUD(g2);
            drawPointsWon(g2);
        }
        //If the game is in the pause state, draw the pause menu
        else if(gp.state == gp.pauseState)
        {
            drawHUD(g2);
            drawPauseMenu(g2);
        }
        else if(gp.state == gp.titleState)
        {
            drawTitleScreen(g2);
        }
        else if(gp.state == gp.settingsState)
        {
            drawSettingsScreen(g2);
        }
        else if(gp.state == gp.controlsState)
        {
            drawControlsScreen(g2);
            if(this.changeKey)
            {
                drawKeyChangeWindow(g2);
            }
        }
        else if(gp.state == gp.gameOverState)
        {
            drawHUD(g2);
            drawGameOverMenu(g2);
        }
    }
}