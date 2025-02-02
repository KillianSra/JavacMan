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

    int commandNb;

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

        y *= 2;
        String text = String.valueOf(gp.player.getScore());
        g2.drawString(text, getXCentered(text, g2, 4), y);
        g2.drawString("100", getXCentered("100", g2, 2) , y);

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
     * Draws the pause menu on the screen.
     *
     * @param g2 Graphics2D instance used for rendering the pause menu
     */
    private void drawPauseMenu(Graphics2D g2)
    {
        //Darkened the screen
        g2.setColor(new Color(0, 0, 0));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

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

        // Draw the "QUIT" option
        y += gp.tileSize * 3;
        g2.drawString("QUIT", getXCentered("QUIT", g2, 2), y);
        if(commandNb == 1)
        {
            g2.drawRoundRect(x - gp.tileSize * 2, (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
        }
    }

    /**
     * Draws the title screen of the game.
     *
     * @param g2 Graphics2D instance used for rendering the title screen elements
     */
    private void drawTitleSceen(Graphics2D g2)
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

        //Draw the "QUIT" option
        y += gp.tileSize * 3;
        g2.drawString("QUIT", getXCentered("QUIT", g2, 2), y);
        if(commandNb == 1)
        {
            g2.drawRoundRect((int) (x - gp.tileSize * 2.75), (int) (y - gp.tileSize * 1.80), gp.tileSize * 8, (int) (gp.tileSize * 2.5), 70, 70);
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


        String points = String.valueOf(Entity.calculatePointsWon());

        if(gp.redGhost.getDisplayPointsWon())
        {
            //Display points earned at the location where the player ate the ghost
            g2.drawString(points, gp.redGhost.getWorldXDead(), gp.redGhost.getWorldYDead() + (gp.tileSize - g2.getFont().getSize() / 2));
        }
        if(gp.blueGhost.getDisplayPointsWon())
        {
            //Display points earned at the location where the player ate the ghost
            g2.drawString(points, gp.blueGhost.getWorldXDead(), gp.blueGhost.getWorldYDead() + (gp.tileSize - g2.getFont().getSize() / 2));
        }
        if(gp.pinkGhost.getDisplayPointsWon())
        {
            //Display points earned at the location where the player ate the ghost
            g2.drawString(points, gp.pinkGhost.getWorldXDead(), gp.pinkGhost.getWorldYDead() + (gp.tileSize - g2.getFont().getSize() / 2));
        }
        if(gp.orangeGhost.getDisplayPointsWon())
        {
            //Display points earned at the location where the player ate the ghost
            g2.drawString(points, gp.orangeGhost.getWorldXDead(), gp.orangeGhost.getWorldYDead() + (gp.tileSize - g2.getFont().getSize() / 2));
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
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / position - length / position;
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


        if(gp.state == gp.playState)
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
            drawTitleSceen(g2);
        }
    }
}