package main;

import object.Coffee;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI
{
    GamePanel gp;
    Font maruMonica;
    BufferedImage playerLife, collectibleItem;

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

        Coffee coffee = new Coffee(this.gp, 0, 0);
        collectibleItem = coffee.getObjectImage();
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
        g2.drawImage(this.collectibleItem, x, y, null);

        //Number of round done
        x += (int) (gp.tileSize * 1.3);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
        g2.drawString(String.valueOf(gp.currentRound), x, y + gp.tileSize);
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

        drawHUD(g2);
    }
}