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

    // Constants are Here
    public final int MOVESPEED = 5;
    public final int WIDTH = 99;   // from image
    public final int HEIGHT = 75;  // from image

    // lmage identifiers
    public enum ImageType
    {
        Left,
        Right,
        Normal,
        Damaged
    }

    // Soudns identifiers
    public enum SoundType
    {
        Laser
    }

    private boolean readyToFire = true;
    private boolean movingLeft = false;
    private boolean movingRight = false;

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private long lastCrashTime;
    private Game game;

    //
    // position at center near the bottom
    public Player(Game game)
    {
        this.game = game;
        x = game.getGraphics().getWidth() / 2;
        y = (int) (game.getGraphics().getHeight() * 0.8);
        lastCrashTime = 0;
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

        // player ship is currently a 1 frame anim (doesn't really need to be an anim)
        anim.addFrame(Assets.player, 1000);
    }
    
    private void updateProjectiles()
    {
        for (int i = 0; i < projectiles.size(); i++)
        {
            Projectile p = projectiles.get(i);
            if (p.isVisible() == true)
            {
                p.update();
            } else
            {
                projectiles.remove(i);
            }
        }
    }

    @Override
    public void update()
    {
        // Moves Character

        x += speedX;
        y += speedY;

        // CLAMP to screen edge
        if (x > game.getGraphics().getWidth())
            x = game.getGraphics().getWidth();
        if (x < 0)
            x = 0;

        if (y > (int)(game.getGraphics().getHeight() *.9) )
            y = (int)(game.getGraphics().getHeight() * .9);
        if (y < (int)(game.getGraphics().getHeight() *.5) )
            y = (int)(game.getGraphics().getHeight() * .5);

//        Log.i("MOOSE", "ctrX=" + x+ ", ctrY=" + y +
//                ", speedX=" + speedX + ", speedY=" + speedY);

        bounds.set(x - (int)(WIDTH*.5), y - (int)(HEIGHT*.5),
                x + (int)(WIDTH*.5), y + (int)(HEIGHT*.5));

        updateProjectiles();

        anim.update(10);
    }

    @Override
    public void draw(Graphics g)
    {
        for (int i = 0; i < projectiles.size(); i++)
        {
            Projectile p = projectiles.get(i);
            p.draw(g);
        }

        // First draw the game elements.
        Image playerSprite = anim.getImage();
        if (getMovingLeft())
            playerSprite = imageList.get(Player.ImageType.Left.ordinal());
        else if (getMovingRight())
            playerSprite = imageList.get(Player.ImageType.Right.ordinal());

        g.drawImage(playerSprite, x - (int) (WIDTH * .5), y - (int) (HEIGHT * .5));
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
            Projectile p = new Projectile(x-2, y - 25);
            projectiles.add(p);

            soundList.get(Player.SoundType.Laser.ordinal()).play(1.0f);
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

    public boolean getMovingLeft()
    {
        return movingLeft;
    }
    public boolean getMovingRight()
    {
        return movingRight;
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
