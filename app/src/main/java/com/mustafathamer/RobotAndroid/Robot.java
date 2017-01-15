package com.mustafathamer.RobotAndroid;

import android.graphics.Rect;
import android.util.Log;

import com.mustafathamer.framework.Game;

import java.util.ArrayList;

import static android.R.attr.width;

/**
 * Created by Mus on 11/26/2016.
 */

public class Robot
{

    // Constants are Here
    public final int MOVESPEED = 5;
    public final int WIDTH = 99;   // from image
    public final int HEIGHT = 75;  // from image

    private Game game;
    private int centerX;
    private int centerY;
    private boolean readyToFire = true;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private int speedX = 0;
    private int speedY = 0;
    public static Rect bounds = new Rect(0, 0, 0, 0);

    private Background bg1 = GameScreen.getBg1();
    private Background bg2 = GameScreen.getBg2();

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private long lastCrashTime;

    //
    // position at center near the bottom
    public Robot(Game game)
    {
        this.game = game;
        centerX = game.getGraphics().getWidth() / 2;
        centerY = (int) (game.getGraphics().getHeight() * 0.8);
        lastCrashTime = 0;
    }

    public void update()
    {
        // Moves Character

        centerX += speedX;
        centerY += speedY;

        // CLAMP to screen edge
        if (centerX > game.getGraphics().getWidth())
            centerX = game.getGraphics().getWidth();
        if (centerX < 0)
            centerX = 0;

        if (centerY > (int)(game.getGraphics().getHeight() *.9) )
            centerY = (int)(game.getGraphics().getHeight() * .9);
        if (centerY < (int)(game.getGraphics().getHeight() *.5) )
            centerY = (int)(game.getGraphics().getHeight() * .5);

//        Log.i("MOOSE", "ctrX=" + centerX+ ", ctrY=" + centerY +
//                ", speedX=" + speedX + ", speedY=" + speedY);

        bounds.set(centerX - (int)(WIDTH*.5), centerY - (int)(HEIGHT*.5),
                centerX + (int)(WIDTH*.5), centerY + (int)(HEIGHT*.5));
    }

    public void stop()
    {
        speedX = speedY = 0;
        movingLeft = movingRight = false;
    }

    public void shoot()
    {
        if (readyToFire)
        {

            Projectile p = new Projectile(centerX-2, centerY - 25);
            projectiles.add(p);
        }
    }

    //
    // play crash time (once every half second)
    //
    public void crash()
    {
        long curTime = System.currentTimeMillis();
        if (curTime - lastCrashTime > 500)
        {
            // TODO - use Assets directly or cache in GameServer copy?
            Assets.playerCrash.play(1.0f);
            lastCrashTime = curTime;
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
    public boolean getMovingLeft()
    {
        return movingLeft;
    }
    public boolean getMovingRight()
    {
        return movingRight;
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
    public void setMovingLeft(boolean movingLeft)
    {
        this.movingLeft = movingLeft;
        if (movingLeft)
            this.movingRight = false;
    }
    public void setMovingRight(boolean movingRight)
    {
        this.movingRight = movingRight;
        if (movingRight)
            this.movingLeft = false;
    }

}
