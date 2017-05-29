package com.mustafathamer.GalaxyRun;

import android.util.Log;

import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;


/**
 * Created by Mus on 11/26/2016.
 * Vertically scrolling backgroundImg, scrolls downward (positive Y)
 */

public class Background
{
    public static final int speedY = 1;
    private int bgX, bgY, idx;
    private Image img;

    public Background(Image img, int x, int y, int idx)
    {
        this.img = img;
        bgX = x;
        bgY = y;
        this.idx = idx;
    }

    public void update()
    {
        if (bgY == 0)
        {
            Log.i("MOOSE", "Background img " + idx + " starting, width=" + img.getWidth() + ", height=" + img.getHeight());
        }
        bgY += speedY;
    }

    public void draw(Graphics g)
    {
        g.drawImage(img, bgX, bgY);
    }

    public int getBgX()
    {
        return bgX;
    }
    public int getBgY()
    {
        return bgY;
    }
    public int getHeight() { return img.getHeight(); }
    public int getWidth() { return img.getWidth(); }
    public int getIndex() { return idx; }

    public void setBgX(int bgX)
    {
        this.bgX = bgX;
    }
    public void setBgY(int bgY)
    {
        this.bgY = bgY;
    }
}
