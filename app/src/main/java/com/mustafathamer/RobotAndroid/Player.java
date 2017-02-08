package com.mustafathamer.RobotAndroid;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;

import java.util.ArrayList;

import static android.R.attr.x;
import static android.R.attr.y;
import static android.R.attr.width;

/**
 * Created by Mus on 11/26/2016.
 */

public class Player extends GameObject
{
    protected ArrayList<Image> imageList = new ArrayList<Image>();

    // Constants are Here
    public final int MOVESPEED = 5;
    public final int WIDTH = 99;   // from image
    public final int HEIGHT = 75;  // from image
    private final int TIME_BETWEEN_SHOTS = 150;     // in millis, 5 shots per sec

    // lmage identifiers
    public enum ImageType
    {
        Left,
        Right,
        Normal,
        Damaged
    }

    // Sounds identifiers
    public enum SoundType
    {
        Laser,
        Crash
    }

    private boolean isShooting = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private long lastCrashTime, lastShootTime;
    private GameScreen gameScreen;
    private int numLives;

    //
    // position at center near the bottom
    public Player(GameScreen game)
    {
        x = GameScreen.gameWidth / 2;
        y = (int) (GameScreen.gameHeight * 0.8);
        this.gameScreen = game;

        lastCrashTime = 0;
        lastShootTime = 0;
        isShooting = false;
        movingLeft = false;
        movingRight = false;
        numLives = 10;
    }

    @Override
    public void initAssets()
    {
        // add images using order of player.ImageType enum
        imageList.add(Assets.playerLeft);
        imageList.add(Assets.playerRight);
        imageList.add(Assets.player);
        imageList.add(Assets.playerDamaged);

        // add sounds using order of player.SoundType enum
        soundList.add(Assets.playerLaser);
        soundList.add(Assets.playerCrash);

        // player ship is currently a 1 frame anim (doesn't really need to be an anim)
        anim.addFrame(Assets.player, 1000);
    }

    @Override
    public void update(float deltaTime)
    {
        // Moves Character
        x += speedX;
        y += speedY;

        // CLAMP to screen edge
        if (x > GameScreen.gameWidth)
            x = GameScreen.gameWidth;
        if (x < 0)
            x = 0;

        if (y > (int)(GameScreen.gameHeight *.9) )
            y = (int)(GameScreen.gameHeight * .9);
        if (y < (int)(GameScreen.gameHeight *.5) )
            y = (int)(GameScreen.gameHeight * .5);

//        Log.i("MOOSE", "ctrX=" + x+ ", ctrY=" + y +
//                ", speedX=" + speedX + ", speedY=" + speedY);

        bounds.set(x - (int)(WIDTH*.5), y - (int)(HEIGHT*.5),
                x + (int)(WIDTH*.5), y + (int)(HEIGHT*.5));

        if (isShooting)
            shoot();

        anim.update((int)(deltaTime * 1000));

        checkCollision();
    }

    @Override
    public void draw(Graphics g)
    {
        // First draw the game elements.
        Image playerSprite = anim.getImage();
        if (getMovingLeft())
            playerSprite = imageList.get(Player.ImageType.Left.ordinal());
        else if (getMovingRight())
            playerSprite = imageList.get(Player.ImageType.Right.ordinal());

        g.drawImage(playerSprite, x - (int) (WIDTH * .5), y - (int) (HEIGHT * .5));
        drawBounds(g);
    }

    //
    // check for collisions with asteroids
    //
    private void checkCollision()
    {
        for (int i=0; i<gameScreen.getGameObjects().size(); i++)
        {
            GameObject gameObject = gameScreen.getGameObjects().get(i);
            if (gameObject.getType() == Type.Asteroid)
            {
                if (gameObject.getBounds().intersect(getBounds()))
                {
                    //Log.i("MOOSE", "checkCollision: HIT");
                    gameObject.setDead(true);   // kill asteroid
                    soundList.get(SoundType.Crash.ordinal()).play(1.0f);
                    numLives = numLives - 1;
                    if (numLives == 0)
                        setDead(true);
                }
            }
        }
    }

    public void stop()
    {
        speedX = speedY = 0;
        movingLeft = movingRight = false;
    }

    private void shoot()
    {
        if (isReadyToFire())
        {
            Projectile p = new Projectile(gameScreen, x-2, y - 25);
            p.initAssets();
            gameScreen.addGameObject(p);
            soundList.get(SoundType.Laser.ordinal()).play(1.0f);
            lastShootTime = System.currentTimeMillis();
        }
    }

    //
    // play crash time (once every half second)
    // wall crash
    //
    public void crash()
    {
        long curTime = System.currentTimeMillis();
        if (curTime - lastCrashTime > 500)
        {
            soundList.get(SoundType.Crash.ordinal()).play(1.0f);
            numLives = numLives - 1;
            if (numLives == 0)
                setDead(true);
            lastCrashTime = curTime;
        }
    }

    public boolean getMovingLeft()
    {
        return movingLeft;
    }
    public boolean getMovingRight()
    {
        return movingRight;
    }

    public boolean isReadyToFire()
    {
        return (System.currentTimeMillis() - lastShootTime) > TIME_BETWEEN_SHOTS;
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

    public int getNumLives()
    {
        return numLives;
    }

    public void setNumLives(int numLives)
    {
        this.numLives = numLives;
    }

    @Override
    public int getWidth() { return WIDTH; }

    @Override
    public int getHeight() { return HEIGHT; }

    public boolean isShooting() { return isShooting;   }
    public void setShooting(boolean shooting)   {  isShooting = shooting;  }

    @Override
    public GameObject.Type getType() { return Type.Player; }

}
