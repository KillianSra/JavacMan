package entity;

import entity.abstracts.Entity;
import entity.enums.Direction;
import main.GamePanel;

import java.awt.*;

public class PlayerAvatar extends Entity
{
    public PlayerAvatar(GamePanel gp)
    {
        super(gp);
        hitbox = new Rectangle(0, 0, 0, 0);
        direction = Direction.RIGHT;
        speed = 2;

        getImage();
        setStartPosition();
    }

    //Methods
    private void setStartPosition()
    {
        worldX = gp.tileSize * -3;
        worldY = gp.tileSize * 10;
    }

    @Override
    public void update()
    {
        super.move();

        //Handle playerAvatar's sprite animation
        spriteCounter++;
        if(spriteCounter > 20)
        {
            if(spriteNum == 1)
            {
                spriteNum = 2;
            }
            else if(spriteNum == 2)
            {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if(worldX >= gp.tileSize * 31)
        {
            setStartPosition();
        }

    }

    @Override
    public void getImage()
    {
        right1 = setup("player/player_right_1", gp.tileSize * 3, gp.tileSize * 3);
        right2 = setup("player/player_right_2", gp.tileSize * 3, gp.tileSize * 3);
    }
}