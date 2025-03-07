package main;

import settings.SettingsManager;

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

        if(gp.state == gp.playState || gp.state == gp.pauseState)
        {
            //Handle the logic based on the current game state
            if(gp.state == gp.playState)
            {
                playState(code);
            }
            else
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
        else if(gp.state == gp.titleState)
        {
            titleState(code);
        }
        else if(gp.state == gp.settingsState)
        {
            settingsState(code);
        }
        else if(gp.state == gp.controlsState)
        {
            controlState(code);
        }
        else if(gp.state == gp.gameOverState)
        {
            gameOverState(code);
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
        handleMenuMovements(code, 2);

        //Execute the selected command when the Enter key is pressed
        if(code == KeyEvent.VK_ENTER)
        {
            switch(gp.UI.commandNb)
            {
                //Resume gameplay
                case 0: gp.state = gp.playState; break;
                //Display settings menu
                case 1:
                    gp.previousState = gp.state;
                    gp.state = gp.settingsState;
                    gp.UI.commandNb = 0;
                    break;
                //Exit the game
                case 2:
                    gp.state = gp.titleState;
                    gp.UI.commandNb = 0;
                    break;
            }
            gp.playSoundEffect(Sound.MENU_SELECTION);
        }
    }

    /**
     * Handles the logic for the title screen state based on user input.
     *
     * @param code Key code representing the user input
     */
    private void titleState(int code)
    {
        handleMenuMovements(code, 2);

        //Execute the selected command when the Enter key is pressed
        if(code == KeyEvent.VK_ENTER)
        {
            switch(gp.UI.commandNb)
            {
                //Start gameplay
                case 0:
                    gp.player.setStartPosition();
                    reset();
                    gp.restart = true;
                    gp.state = gp.playState;
                    gp.playSoundEffect(Sound.MENU_SELECTION);
                    break;
                //Display settings menu
                case 1:
                    gp.previousState = gp.state;
                    gp.state = gp.settingsState;
                    gp.UI.commandNb = 0;
                    break;
                //Exit the game
                case 2: System.exit(0); break;
            }
        }
    }

    /**
     * Handles the logic for the settings state based on user input.
     *
     * @param code Key code representing the user input
     */
    private void settingsState(int code)
    {
        handleMenuMovements(code, 4);

        //Sound effects volume
        if(gp.UI.commandNb == 0)
        {
            //Decrease the sound effect volume
            if((code == KeyEvent.VK_Q || code == KeyEvent.VK_LEFT) && gp.sound.volumeScale != 0)
            {
                gp.sound.volumeScale--;
                gp.sound.setVolumeLevel();
                gp.playSoundEffect(Sound.BAR_MOVEMENT);
            }
            //Increase the sound effect volume
            else if((code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) && gp.sound.volumeScale != 5)
            {
                gp.sound.volumeScale++;
                gp.sound.setVolumeLevel();
                gp.playSoundEffect(Sound.BAR_MOVEMENT);
            }
        }
        //Toggle between full screen and basic screen
        else if(gp.UI.commandNb == 1)
        {
            if(code == KeyEvent.VK_ENTER)
            {
                gp.fullScreen = !gp.fullScreen;
                Main.toggleFullScreen();
            }
        }
        //Display FPS counter
        else if(gp.UI.commandNb == 2)
        {
            if(code == KeyEvent.VK_ENTER)
            {
                gp.displayFPSCounter = !gp.displayFPSCounter;
            }
        }
        else if(gp.UI.commandNb == 3)
        {
            if(code == KeyEvent.VK_ENTER)
            {
                gp.UI.commandNb = 0;
                gp.state = gp.controlsState;
            }
        }
        //Save settings
        else if(gp.UI.commandNb == 4)
        {
            if(code == KeyEvent.VK_ENTER)
            {
                SettingsManager.save(gp.sound.volumeScale, gp.fullScreen, gp.displayFPSCounter);
                gp.state = gp.previousState;
                gp.UI.commandNb = 0;
            }
        }

        //Return to the previous state
        if(code == KeyEvent.VK_ESCAPE)
        {
            gp.state = gp.previousState;
        }
    }

    /**
     * Handles the logic for the controls state based on user input.
     *
     * @param code Key code representing the user input
     */
    private void controlState(int code)
    {
        //Return to settings menu
        if(code == KeyEvent.VK_ESCAPE)
        {
            gp.state = gp.settingsState;
        }
    }

    /**
     * Handles the logic for the game over screen state based on user input
     *
     * @param code Key code representing the user input
     */
    private void gameOverState(int code)
    {
        handleMenuMovements(code, 1);

        //Execute the selected command when the Enter key is pressed
        if(code == KeyEvent.VK_ENTER)
        {
            switch(gp.UI.commandNb)
            {
                //Restart the game
                case 0:
                    gp.player.setStartPosition();
                    reset();
                    gp.restart = true;
                    gp.state = gp.playState;
                    break;
                //Back to the main menu
                case 1:
                    gp.state = gp.titleState;
                    gp.UI.commandNb = 0;
            }
            gp.playSoundEffect(Sound.MENU_SELECTION);
        }
    }

    /**
     * Handles menu navigation based on user input.
     *
     * @param code Key code representing the user input
     * @param max  Maximum index for the menu options
     */
    private void handleMenuMovements(int code, int max)
    {
        //Navigate the title menu using the up and down keys
        //Move up in the menu
        if(code == KeyEvent.VK_Z || code == KeyEvent.VK_UP)
        {
            gp.UI.commandNb--;
            if(gp.UI.commandNb < 0)
            {
                gp.UI.commandNb = max;
            }
            gp.playSoundEffect(Sound.MENU_NAVIGATION);
        }
        //Move down in the menu
        else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
        {
            gp.UI.commandNb++;
            if(gp.UI.commandNb > max)
            {
                gp.UI.commandNb = 0;
            }
            gp.playSoundEffect(Sound.MENU_NAVIGATION);
        }
    }
}
