package com.mustafathamer.RobotAndroid;

import android.util.Log;

/**
 * Created by Mus on 11/26/2016.
 * Vertically scrolling background, scrolls downward (positive Y)
 */

public class Background
{
    private int bgX, bgY, speedY;
    static private int width = 480;
    static private int height = 2160;

    public Background(int x, int y)
    {
        bgX = x;
        bgY = y;
        speedY = 2;
    }

    public void update()
    {
        bgY += speedY;

        if (bgY >= height)
        {
            bgY  = -height;
        }
    }

    public int getBgX()
    {
        return bgX;
    }
    public int getBgY()
    {
        return bgY;
    }
    public int getSpeedY()
    {
        return speedY;
    }
    static public int getHeight() { return height; }
    static public int getWidth() { return width; }

    public void setBgX(int bgX)
    {
        this.bgX = bgX;
    }
    public void setBgY(int bgY)
    {
        this.bgY = bgY;
    }
    public void setSpeedY(int speedY)
    {
        this.speedY = speedY;
    }
}
