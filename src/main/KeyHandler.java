package main;

import storage.controls.Controls;
import storage.controls.ControlsManager;
import storage.settings.SettingsManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class KeyHandler implements KeyListener
{
    GamePanel gp;
    Controls controls;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public int up, down, left, right, pause;

    //Used in key binding changes.
    public int keyCode = -1;

    public KeyHandler(GamePanel gp)
    {
        this.gp = gp;

        //Create the controls.dat file if necessary
        ControlsManager.initializeControls();

        //retrieve controls from controls.dat file
        this.controls = ControlsManager.load();
        loadKey();
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
            if(code == this.pause)
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
        if(code == this.up || code == KeyEvent.VK_UP)
        {
            this.upPressed = true;
            this.downPressed = false;
            this.leftPressed = false;
            this.rightPressed = false;
        }
        //Move down
        else if(code == this.down || code == KeyEvent.VK_DOWN)
        {
            this.upPressed = false;
            this.downPressed = true;
            this.leftPressed = false;
            this.rightPressed = false;
        }
        //Move left
        else if(code == this.left || code == KeyEvent.VK_LEFT)
        {
            this.upPressed = false;
            this.downPressed = false;
            this.leftPressed = true;
            this.rightPressed = false;
        }
        //Move right
        else if(code == this.right || code == KeyEvent.VK_RIGHT)
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

                    //Reset ghosts properties
                    gp.redGhost.reset();
                    gp.pinkGhost.reset();
                    gp.blueGhost.reset();
                    gp.orangeGhost.reset();

                    //Reset score
                    gp.player.setScore(0);
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
                    gp.state = gp.readyState;
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
            if((code == this.left || code == KeyEvent.VK_LEFT) && gp.sound.volumeScale != 0)
            {
                gp.sound.volumeScale--;
                gp.sound.setVolumeLevel();
                gp.playSoundEffect(Sound.BAR_MOVEMENT);
            }
            //Increase the sound effect volume
            else if((code == this.right || code == KeyEvent.VK_RIGHT) && gp.sound.volumeScale != 5)
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
        if(code == this.pause)
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
        //Navigating the "Controls" menu
        if(!gp.UI.changeKey)
        {
            handleMenuMovements(code, 5);

            //Change the selected key
            if(code == KeyEvent.VK_ENTER && gp.UI.commandNb != 5)
            {
                gp.UI.changeKey = true;
            }
            //Save key change to .dat file
            else if(code == KeyEvent.VK_ENTER)
            {
                if(validatedKeys())
                {
                    ControlsManager.save(this.up, this.down, this.left, this.right, this.pause);
                    gp.state = gp.settingsState;
                    gp.UI.commandNb = 0;
                }
                else
                {
                    gp.playSoundEffect(Sound.ERROR);
                }
            }
        }
        //Change the key associated with an action
        else
        {
            //We exclude the directional arrows and the "enter" key
            if(code != KeyEvent.VK_UP && code != KeyEvent.VK_DOWN && code != KeyEvent.VK_LEFT &&
                    code != KeyEvent.VK_RIGHT && code != KeyEvent.VK_ENTER)
            {
                this.keyCode = code;
            }

            //If the enter key is pressed on the 'save' button
            if(code == KeyEvent.VK_ENTER && keyCode != -1)
            {
                changeKey();
            }
        }

        if(code == this.pause)
        {
            //Return to settings menu
            if(!gp.UI.changeKey)
            {
                //Exits the page without saving the key changes.
                loadKey();
                gp.state = gp.settingsState;
            }
            //Cancels the key change
            else
            {
                gp.UI.changeKey = false;
                this.keyCode = -1;
            }
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
                    reset();
                    gp.restart = true;

                    //Reset entities
                    gp.player.setStartPosition();
                    gp.redGhost.reset();
                    gp.pinkGhost.reset();
                    gp.blueGhost.reset();
                    gp.orangeGhost.reset();

                    gp.state = gp.readyState;
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
        if(code == this.up || code == KeyEvent.VK_UP)
        {
            gp.UI.commandNb--;
            if(gp.UI.commandNb < 0)
            {
                gp.UI.commandNb = max;
            }
            gp.playSoundEffect(Sound.MENU_NAVIGATION);
        }
        //Move down in the menu
        else if(code == this.down || code == KeyEvent.VK_DOWN)
        {
            gp.UI.commandNb++;
            if(gp.UI.commandNb > max)
            {
                gp.UI.commandNb = 0;
            }
            gp.playSoundEffect(Sound.MENU_NAVIGATION);
        }
    }

    /**
     * Loads the key bindings from the controls configuration.
     */
    private void loadKey()
    {
        this.up = this.controls.upCode();
        this.down = this.controls.downCode();
        this.left = this.controls.leftCode();
        this.right = this.controls.rightCode();
        this.pause = this.controls.pauseCode();
    }

    /**
     * Retrieves the textual representation of a key based on its keycode.
     *
     * @param keycode the integer code of the key
     * @return the name of the key as a String
     */
    public String getKeyByKeycode(int keycode)
    {
        String key;

        //Since the ZQSD keys are associated with the directional arrows on an AZERTY keyboard, we use the key code
        //directly to return the correct letter.
        switch(keycode)
        {
            case KeyEvent.VK_Z: key = "[Z]"; break;
            case KeyEvent.VK_S: key = "[S]"; break;
            case KeyEvent.VK_Q: key = "[Q]"; break;
            case KeyEvent.VK_D: key = "[D]"; break;
            default: key = '[' + KeyEvent.getKeyText(keycode) + ']';
        }
        return key;
    }

    /**
     * Updates the key binding for a specific action based on the current selection.
     */
    private void changeKey()
    {
        //Assign the key based on the selected action
        switch(gp.UI.commandNb)
        {
            case 0: this.up = keyCode; break;
            case 1: this.down = keyCode; break;
            case 2: this.left = keyCode; break;
            case 3: this.right = keyCode; break;
            case 4: this.pause = keyCode; break;
        }

        gp.UI.changeKey = false;
        this.keyCode = -1;
    }

    /**
     * Validates that each control key is assigned a unique value,
     * ensuring no duplicate keys are used for different actions.
     *
     * @return {@code true} if all assigned keys are unique and properly set, {@code false} otherwise.
     */
    private boolean validatedKeys()
    {
        boolean isValidate = false;

        int[] keys = {up, down, left, right, left, pause};

        //Check if the value of each variable is different from the others
        Set<Integer> checkedKeys = new HashSet<>();
        for(int key : keys)
        {
            //We make sure that each action has an assigned key
            if(key != -1 && !Objects.equals(getKeyByKeycode(key), "[Unknown keyCode: 0x0]"))
            {
                checkedKeys.add(key);
            }
        }

        if(checkedKeys.size() == 5)
        {
            isValidate = true;
        }

        return isValidate;
    }
}
