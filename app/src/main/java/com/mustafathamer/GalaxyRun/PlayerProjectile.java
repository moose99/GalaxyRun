package com.mustafathamer.GalaxyRun;

import android.graphics.Rect;
import android.util.Log;

import com.mustafathamer.util.Assert;
import com.mustafathamer.framework.Graphics;


/**
 * Created by Mus on 11/26/2016.
 */

public class PlayerProjectile extends GameObject
{
    private GameScreen gameScreen;
    private Rect laserRect;

    @Override     public int getWidth() { return laserRect.width(); }
    @Override     public int getHeight() { return laserRect.height(); }
    @Override     public GameObject.Type getType() { return Type.PlayerProjectile; }

    // Sounds identifiers
    public enum SoundType
    {
        Explode
    }

    public PlayerProjectile(GameScreen game)
    {
        speedY = 7;
        this.gameScreen = game;
    }

    @Override
    public void initAssets()
    {
        // add sounds using order of SoundType enum
        soundList.add(Assets.explosion);

        laserRect = Assets.ssReduxSprites.getRect("laserRed03.png");
        Assert.Assert(laserRect != null);
    }
    //
    // Moves upwards until it goes off the screen
    @Override
    public void update(float deltaTime)
    {
        y -= speedY;
        updateBounds();
        if (!checkIfOffScreen())
        {
            checkCollision();
        }
    }

    @Override
    public void draw(Graphics g)
    {
        //g.drawRect(x, y, WIDTH, HEIGHT, Color.YELLOW);
        g.drawImage(Assets.ssReduxSprites.getImage(),           // image
                getBounds().left, getBounds().top,              // x, y (upper left of sprite)
                laserRect.left, laserRect.top,                  // srcx, srcy
                laserRect.width(), laserRect.height());         // width, height

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
            if (gameObject.getType() == Type.Asteroid || gameObject.getType() == Type.Alien)
            {
                if (gameObject.getBounds().intersect(getBounds()))
                {
//                    Log.i("MOOSE", "checkCollision: HIT");
                    gameObject.setDead(true);   // kill object
                    setDead(true);              // kill me
                    soundList.get(SoundType.Explode.ordinal()).play(1.0f);
                    gameScreen.setScore(gameScreen.getScore() + 1);
                }
            }
        }
    }
}
