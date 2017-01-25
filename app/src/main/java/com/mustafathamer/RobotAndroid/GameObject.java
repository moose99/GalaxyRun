package com.mustafathamer.RobotAndroid;

import android.graphics.Rect;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Image;
import com.mustafathamer.framework.Sound;

import java.util.ArrayList;

/**
 * Created by Mus on 1/24/2017.
 * Base class for game objects, like asteroids, aliens, etc
 */

public class GameObject
{
    protected ArrayList<Image> imageList = new ArrayList<Image>();
    protected ArrayList<Sound> soundList = new ArrayList<Sound>();

    protected Rect bounds = new Rect();
    protected Animation anim = new Animation();
    protected int x = 0, y = 0;                 // center
    protected int speedX = 0, speedY = 0;       // velocity
    protected Game game;

    public GameObject(Game game)
    {
        this.game = game;
    }

    public GameObject()
    {

    }

    public ArrayList<Image> getImageList()
    {
        return imageList;
    }

    public void setImageList(ArrayList<Image> images)
    {
        this.imageList = images;
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
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


}
