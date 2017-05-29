package com.mustafathamer.GalaxyRun;

import android.graphics.Color;
import android.graphics.Rect;

import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Sound;

import java.util.ArrayList;

/**
 * Created by Mus on 1/24/2017.
 * Abstract base class for game objects, like asteroids, aliens, projectiles, powerups, etc.
 * x,y position represents the center of the object
 */

public abstract class GameObject
{
    // DEBUGGING
    private boolean drawBounds = false;

    protected enum Type
    {
        Player,
        Asteroid,
        PlayerProjectile,
        AlienProjectile,
        PowerUp,
        Alien
    }

    protected ArrayList<Sound> soundList = new ArrayList<Sound>();

    protected Rect bounds = new Rect();
    protected Animation anim = new Animation();
    protected float x = 0, y = 0;               // center
    protected int speedX = 0, speedY = 0;       // velocity
    protected boolean dead = false;             // set dead when it should be removed the game
    protected Type type;

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

    final public boolean bounceOffWalls()
    {
        // bounce horizontally off the walls
        int border = 32;
        if ( (getBounds().right >= GameScreen.gameWidth - border) || getBounds().left <= border )
        {
            speedX = -speedX;
            return true;
        }
        return false;
    }

    final public boolean checkIfOffScreen()
    {
        // set to remove if we go off the bottom of the screen, return true
        if (getBounds().top > GameScreen.gameHeight || getBounds().bottom < 0)
        {
            setDead(true);
            return true;
        }

        return false;
    }

    // DEBUG ONLY
    public void drawBounds(Graphics g)
    {
        if (drawBounds)
            g.strokeRect(bounds.left, bounds.top, bounds.width(), bounds.height(), Color.RED);
    }

    //
    // update bounds based on x,y and width, height
    // assume x,y is object center
    //
    private void updateBounds(int w, int h)
    {
        bounds.set(Math.round(x - w/2.f), Math.round(y - h/2.f),     // left, top
                Math.round(x + w/2.f), Math.round(y + h/2.f));      // right, bottom
    }

    final public void updateBounds()
    {
        updateBounds(getWidth(), getHeight());
    }

    final public Rect getBounds()
    {
        return bounds;
    }
    final public void setBounds(Rect bounds)
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

    final public float getX()
    {
        return x;
    }
    final public int getRoundX()
    {
        return Math.round(x);
    }
    final public void setX(float x)
    {
        this.x = x;
    }

    final public float getY()
    {
        return y;
    }
    final public int getRoundY()
    {
        return Math.round(y);
    }
    final public void setY(float y)
    {
        this.y = y;
    }

    //
    // sets the position (object center)
    //
    final public void setPos(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    final public int getSpeedX()
    {
        return speedX;
    }
    final public void setSpeedX(int speedX)
    {
        this.speedX = speedX;
    }

    final  public int getSpeedY()
    {
        return speedY;
    }
    final public void setSpeedY(int speedY)
    {
        this.speedY = speedY;
    }

    final public ArrayList<Sound> getSoundList()
    {
        return soundList;
    }
    final public void setSoundList(ArrayList<Sound> soundList)
    {
        this.soundList = soundList;
    }

    final public boolean isDead()
    {
        return dead;
    }
    final public void setDead(boolean dead)
    {
        this.dead = dead;
    }

    // ABSTRACT METHODS
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract Type getType();
}
