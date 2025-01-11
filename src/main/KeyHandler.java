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

        //Handle the logic based on the current game state
        if(gp.state == gp.playState)
        {
            playState(code);
        }
        else if(gp.state == gp.pauseState)
        {
            pauseState(code);
        }

        //Toggle between play and pause states when the Escape key is pressed
        if(code == KeyEvent.VK_ESCAPE)
        {
            if(gp.state == gp.playState)
            {
                gp.state = gp.pauseState;
                //Reset
                gp.UI.subState = gp.UI.pauseState;
                gp.UI.commandNb = 0;
            }
            else
            {
                gp.state = gp.playState;
            }
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

    /**
     * Handles key inputs during gameplay.
     *
     * @param code the keycode of the pressed key
     */
    private void playState(int code)
    {
        //Handle player movements
        //Move up
        if(code == KeyEvent.VK_Z || code == KeyEvent.VK_UP)
        {
            this.upPressed = true;
            this.downPressed = false;
            this.leftPressed = false;
            this.rightPressed = false;
        }
        //Move down
        else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
        {
            this.upPressed = false;
            this.downPressed = true;
            this.leftPressed = false;
            this.rightPressed = false;
        }
        //Move left
        else if(code == KeyEvent.VK_Q || code == KeyEvent.VK_LEFT)
        {
            this.upPressed = false;
            this.downPressed = false;
            this.leftPressed = true;
            this.rightPressed = false;
        }
        //Move right
        else if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT)
        {
            this.upPressed = false;
            this.downPressed = false;
            this.rightPressed = true;
            this.leftPressed = false;
        }
    }

    /**
     * Handles key inputs while the game is paused.
     *
     * @param code the keycode of the pressed key
     */
    private void pauseState(int code)
    {
        //Navigate the pause menu using the up and down keys
        //Move up in the menu
        if(code == KeyEvent.VK_Z || code == KeyEvent.VK_UP)
        {
            gp.UI.commandNb--;
            if(gp.UI.commandNb < 0)
            {
                gp.UI.commandNb = 1;
            }
        }
        //Move down in the menu
        else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
        {
            gp.UI.commandNb++;
            if(gp.UI.commandNb > 1)
            {
                gp.UI.commandNb = 0;
            }
        }

        //Execute the selected command when the Enter key is pressed
        if(code == KeyEvent.VK_ENTER)
        {
            switch(gp.UI.commandNb)
            {
                //Resume gameplay
                case 0: gp.state = gp.playState; break;
                //Exit the game
                case 1: System.exit(0); break;
            }
        }
    }
}
