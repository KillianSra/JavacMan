package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener
{

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp)
    {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) { /* Not used */ }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();

        //Handle player movements
        if(code == KeyEvent.VK_Z || code == KeyEvent.VK_UP)
        {
            this.upPressed = true;
            this.downPressed = false;
            this.leftPressed = false;
            this.rightPressed = false;
        }
        else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
        {
            this.upPressed = false;
            this.downPressed = true;
            this.leftPressed = false;
            this.rightPressed = false;
        }
        else if(code == KeyEvent.VK_Q || code == KeyEvent.VK_LEFT)
        {
            this.upPressed = false;
            this.downPressed = false;
            this.leftPressed = true;
            this.rightPressed = false;
        }
        else if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT)
        {
            this.upPressed = false;
            this.downPressed = false;
            this.rightPressed = true;
            this.leftPressed = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { /* Not used */ }

    /**
     * Resets all directional inputs to their default state.
     */
    public void reset()
    {
        this.upPressed = false;
        this.downPressed = false;
        this.rightPressed = false;
        this.leftPressed = false;
    }
}
