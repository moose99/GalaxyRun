package com.mustafathamer.GalaxyRun;

import android.graphics.Matrix;

import com.mustafathamer.util.Assert;
import com.mustafathamer.util.Vector2D;
import com.mustafathamer.framework.Graphics;


/**
 * Created by Mus on 5/7/2017.
 */

public class Alien extends GameObject
{
    // Sounds identifiers
    public enum SoundType
    {
        Laser,
        Crash,
        Explosion
    }

    private final int TIME_BETWEEN_SHOTS = 2000;     // in millis
    private int alienIdx;
    private Player player = GameScreen.getPlayer();
    private Matrix mat;
    private long lastShootTime;
    private GameScreen gameScreen;

    @Override public  int getWidth()   {        return Assets.aliens[alienIdx].getWidth();    }
    @Override public int getHeight()   {        return Assets.aliens[alienIdx].getHeight();    }
    @Override public Type getType()    {        return Type.Alien;    }

    //
    //
    //
    public Alien(int alienIdx, GameScreen game)
    {
        this.alienIdx = alienIdx;
        Assert.Assert(alienIdx >= 0 && alienIdx < Assets.numAliens);
        mat = new Matrix();
        this.gameScreen = game;
        speedY = Background.speedY * 3;

        lastShootTime = 0;
    }

    //
    //
    //
    @Override
    public void initAssets()
    {
        anim.addFrame(Assets.aliens[alienIdx], 1000);   // currently just a 1 frame animation (ie. not animated)

        // add sounds using order of player.SoundType enum
        soundList.add(Assets.playerLaser);
        soundList.add(Assets.playerCrash);
        soundList.add(Assets.explosion);

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
        mat.postTranslate(getBounds().left, getBounds().top);

        g.drawImage(anim.getImage(), mat);
        drawBounds(g);
    }

    public void update(float deltaTime)
    {
        if (!gameScreen.singleAlien)
        {
            y += speedY;
            x += speedX;
        }
        updateBounds();
        bounceOffWalls();
        if (!checkIfOffScreen())
            shoot();
    }

    private void shoot()
    {
        if (isReadyToFire())
        {
            AlienProjectile p = new AlienProjectile(gameScreen);
            p.initAssets();
            /*
            p.setPos((getBounds().centerX() - Math.round(p.getWidth()*.5f)),
                    (getBounds().centerY() - Math.round(p.getHeight()*.5f)) );
            p.updateBounds();
            */
            p.setPos(getBounds().centerX(), getBounds().centerY());
            p.updateBounds();
            p.calcDirection();
            gameScreen.addGameObject(p);
            soundList.get(SoundType.Laser.ordinal()).play(1.0f);
            lastShootTime = System.currentTimeMillis();
        }
    }

    public boolean isReadyToFire()
    {
        return (System.currentTimeMillis() - lastShootTime) > TIME_BETWEEN_SHOTS;
    }

}