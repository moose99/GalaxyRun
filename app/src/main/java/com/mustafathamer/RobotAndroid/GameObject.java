package com.mustafathamer.RobotAndroid;

import android.graphics.Rect;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;
import com.mustafathamer.framework.Sound;

import java.util.ArrayList;

/**
 * Created by Mus on 1/24/2017.
 * Base class for game objects, like asteroids, aliens, etc
 */

public class GameObject
{
    protected ArrayList<Sound> soundList = new ArrayList<Sound>();

    protected Rect bounds = new Rect();
    protected Animation anim = new Animation();
    protected int x = 0, y = 0;                 // center
    protected int speedX = 0, speedY = 0;       // velocity
    protected boolean visible = false;

    public boolean isDead()
    {
        return dead;
    }

    public void setDead(boolean dead)
    {
        this.dead = dead;
    }

    protected boolean dead = false;                     // set dead when it should be removed the game

    public GameObject()
    {

    }

    public void initAssets()
    {

    }

    public void draw(Graphics g)
    {

    }

    public void update(float deltaTime)
    {

    }

    public Rect getBounds()
    {
        return bounds;
    }

    public void setBounds(Rect bounds)
    {
        this.bounds = bounds;
    }

    public Animation getAnim()
    {
        return anim;
    }

    public void setAnim(Animation anim)
    {
        this.anim = anim;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setPos(int x, int y) { this.x = x; this.y = y; }

    public int getSpeedX()
    {
        return speedX;
    }

    public void setSpeedX(int speedX)
    {
        this.speedX = speedX;
    }

    public int getSpeedY()
    {
        return speedY;
    }

    public void setSpeedY(int speedY)
    {
        this.speedY = speedY;
    }

    public ArrayList<Sound> getSoundList()
    {
        return soundList;
    }

    public void setSoundList(ArrayList<Sound> soundList)
    {
        this.soundList = soundList;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

}
