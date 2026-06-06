package io.github.killiansra.javacman.entity;

import io.github.killiansra.javacman.entity.abstracts.Entity;
import io.github.killiansra.javacman.entity.enums.Direction;
import io.github.killiansra.javacman.main.GamePanel;

import java.awt.*;

public class PlayerAvatar extends Entity
{
    public PlayerAvatar(GamePanel gp)
    {
        super(gp);
        movementHitbox = new Rectangle(0, 0, 0, 0);
        collisionHitbox = new Rectangle(0, 0, 0, 0);
        direction = Direction.RIGHT;
        speed = 2;

        getImage();
        setStartPosition();
    }

    //Methods
    @Override
    public void setStartPosition()
    {
        worldX = gp.tileSize * -3;
        worldY = gp.tileSize * 10;
    }

    @Override
    public void update()
    {
        super.move();

        //Handle playerAvatar's sprite animation
        spriteAnimation();

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