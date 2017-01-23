package com.mustafathamer.RobotAndroid;

import android.graphics.Rect;

/**
 * Created by Mus on 11/26/2016.
 */

public class Enemy
{
    private int power, centerX, speedX, centerY;
    private Background bg = null;
    private Player player = GameScreen.getPlayer();

    public Rect r = new Rect(0, 0, 0, 0);
    public int health = 5;

    private int movementSpeed;

    // Behavioral Methods
    public void update()
    {
        follow();
        centerX += speedX;
        speedX = Background.speedY * 5 + movementSpeed;
        r.set(centerX - 25, centerY - 25, centerX + 25, centerY + 35);

        if (Rect.intersects(r, Player.bounds))
        {
            checkCollision();
        }


    }

    private void checkCollision()
    {
        if (Rect.intersects(r, Player.bounds))
        {

        }
    }

    public void follow()
    {

        if (centerX < -95 || centerX > 810)
        {
            movementSpeed = 0;
        } else if (Math.abs(player.getCenterX() - centerX) < 5)
        {
            movementSpeed = 0;
        } else
        {

            if (player.getCenterX() >= centerX)
            {
                movementSpeed = 1;
            } else
            {
                movementSpeed = -1;
            }
        }

    }

    public void die()
    {

    }

    public void attack()
    {

    }

    public int getPower()
    {
        return power;
    }

    public int getSpeedX()
    {
        return speedX;
    }

    public int getCenterX()
    {
        return centerX;
    }

    public int getCenterY()
    {
        return centerY;
    }

    public Background getBg()
    {
        return bg;
    }

    public void setPower(int power)
    {
        this.power = power;
    }

    public void setSpeedX(int speedX)
    {
        this.speedX = speedX;
    }

    public void setCenterX(int centerX)
    {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY)
    {
        this.centerY = centerY;
    }

    public void setBg(Background bg)
    {
        this.bg = bg;
    }

}