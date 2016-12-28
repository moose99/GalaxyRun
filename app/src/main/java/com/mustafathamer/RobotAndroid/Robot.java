package com.mustafathamer.RobotAndroid;

import android.graphics.Rect;

import com.mustafathamer.framework.Game;

import java.util.ArrayList;

/**
 * Created by Mus on 11/26/2016.
 */

public class Robot
{

    // Constants are Here
    public static int MOVESPEED = 5;

    private Game game;
    private int centerX;
    private int centerY;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean readyToFire = true;

    private int speedX = 0;
    private int speedY = 0;
    public static Rect rect = new Rect(0, 0, 0, 0);
    public static Rect rect2 = new Rect(0, 0, 0, 0);
    public static Rect rect3 = new Rect(0, 0, 0, 0);
    public static Rect rect4 = new Rect(0, 0, 0, 0);
    public static Rect yellowRed = new Rect(0, 0, 0, 0);

    public static Rect footleft = new Rect(0, 0, 0, 0);
    public static Rect footright = new Rect(0, 0, 0, 0);

    private Background bg1 = GameScreen.getBg1();
    private Background bg2 = GameScreen.getBg2();

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    //
    // position at center near the bottom
    public Robot(Game game)
    {
        this.game = game;
        centerX = game.getGraphics().getWidth() / 2;
        centerY = (int) (game.getGraphics().getHeight() * 0.8);
    }

    public void update()
    {
        // Moves Character

        centerX += speedX;

        // CLAMP
        if (centerX > game.getGraphics().getWidth())
            centerX = game.getGraphics().getWidth();
        if (centerX < 0)
            centerX = 0;

        rect.set(centerX - 34, centerY - 63, centerX + 34, centerY);
        rect2.set(rect.left, rect.top + 63, rect.left + 68, rect.top + 128);
        rect3.set(rect.left - 26, rect.top + 32, rect.left, rect.top + 52);
        rect4.set(rect.left + 68, rect.top + 32, rect.left + 94, rect.top + 52);
        yellowRed.set(centerX - 110, centerY - 110, centerX + 70, centerY + 70);
        footleft.set(centerX - 50, centerY + 20, centerX, centerY + 35);
        footright.set(centerX, centerY + 20, centerX + 50, centerY + 35);
    }

    public void moveRight()
    {
        speedX = MOVESPEED;
    }
    public void moveLeft()  { speedX = -MOVESPEED; }

    public void move(int amt)
    {
        speedX = amt;
        if (amt==0)
            stop();
    }

    public void stopRight()
    {
        setMovingRight(false);
        stop();
    }

    public void stopLeft()
    {
        setMovingLeft(false);
        stop();
    }

    public void stop()
    {
        setMovingLeft(false);
        setMovingRight(false);
        speedX = 0;
    }

    public void jump()
    {
        /*
        if (jumped == false)
        {
            speedY = JUMPSPEED;
            jumped = true;
        }
        */

    }

    public void shoot()
    {
        if (readyToFire)
        {
            Projectile p = new Projectile(centerX + 50, centerY - 25);
            projectiles.add(p);
        }
    }

    public int getCenterX()
    {
        return centerX;
    }

    public int getCenterY()
    {
        return centerY;
    }

    public int getSpeedX()
    {
        return speedX;
    }

    public int getSpeedY()
    {
        return speedY;
    }

    public void setCenterX(int centerX)
    {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY)
    {
        this.centerY = centerY;
    }

    public void setSpeedX(int speedX)
    {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY)
    {
        this.speedY = speedY;
    }

    public boolean isMovingRight()
    {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight)
    {
        this.movingRight = movingRight;
    }

    public boolean isMovingLeft()
    {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft)
    {
        this.movingLeft = movingLeft;
    }

    public ArrayList getProjectiles()
    {
        return projectiles;
    }

    public boolean isReadyToFire()
    {
        return readyToFire;
    }

    public void setReadyToFire(boolean readyToFire)
    {
        this.readyToFire = readyToFire;
    }

}
