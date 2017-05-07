package com.mustafathamer.GalaxyRun;

import android.graphics.Matrix;

import com.mustafathamer.util.Assert;
import com.mustafathamer.util.Vector2D;
import com.mustafathamer.framework.Graphics;


/**
 * Created by Mus on 5/7/2017.
 * x, y position specifies the upper left of the object
 */

public class Alien extends GameObject
{
    private int alienIdx;
    private Player player = GameScreen.getPlayer();
    private Matrix mat;

    //
    //
    //
    public Alien(int alienIdx)
    {
        this.alienIdx = alienIdx;
        Assert.Assert(alienIdx >= 0 && alienIdx < Assets.numAliens);
        mat = new Matrix();
    }

    //
    //
    //
    @Override
    public void initAssets()
    {
        anim.addFrame(Assets.aliens[alienIdx], 1000);   // currently just a 1 frame animation (ie. not animated)
        speedY = Background.speedY * 2;
    }

    @Override
    public void draw(Graphics g)
    {
        Vector2D ctr = new Vector2D(getBounds().centerX(), getBounds().centerY());
        Vector2D playerCtr = new Vector2D(player.getBounds().centerX(), player.getBounds().centerY());
        Vector2D dir = playerCtr.subtract(ctr);
        dir.normalize();

        Vector2D yAxis = new Vector2D(0, 1);
        double dot = yAxis.dotProduct(dir);
        float deg = (float)Math.toDegrees(Math.acos(dot));
        if (dir.getX() > 0)     // check if we need to rotate left vs right
            deg = -deg;

        mat.reset();
        mat.postTranslate(-getWidth()/2.0f, -getHeight()/2.0f);
        mat.postRotate(deg);
        mat.postTranslate(getWidth()/2.0f, getHeight()/2.0f);
        mat.postTranslate(x, y);

        g.drawImage(anim.getImage(), mat);
        drawBounds(g);
    }

    public void update(float deltaTime)
    {
        y += speedY;
        x += speedX;

        updateBounds(getWidth(), getHeight());
        bounceOffWalls();
        checkBelowScreen();
    }

    // ABSTRACT METHODS
    public int getWidth()    {        return Assets.aliens[alienIdx].getWidth();    }
    public int getHeight()   {        return Assets.aliens[alienIdx].getHeight();    }
    public Type getType()    {        return Type.Alien;    }

}