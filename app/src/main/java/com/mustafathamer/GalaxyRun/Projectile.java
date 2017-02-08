package com.mustafathamer.GalaxyRun;

import android.graphics.Color;
import android.util.Log;

import com.mustafathamer.framework.Graphics;


/**
 * Created by Mus on 11/26/2016.
 */

public class Projectile extends GameObject
{
    private static final int WIDTH = 4;
    private static final int HEIGHT = 4;
    private GameScreen gameScreen;

    // Sounds identifiers
    public enum SoundType
    {
        Explode
    }

    public Projectile(GameScreen game, int startX, int startY)
    {
        x = startX;
        y = startY;
        speedY = 7;

        this.gameScreen = game;
    }

    @Override
    public void initAssets()
    {
        // add sounds using order of SoundType enum
        soundList.add(Assets.explosion);
    }
    //
    // Moves upwards until it goes off the screen
    @Override
    public void update(float deltaTime)
    {
        y -= speedY;
        updateBounds(WIDTH, HEIGHT);
        if (y < 0)
        {
            setDead(true);
        }
        else
        {
            checkCollision();
        }
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawRect(x, y, WIDTH, HEIGHT, Color.YELLOW);
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
                    Log.i("MOOSE", "checkCollision: HIT");
                    gameObject.setDead(true);   // kill asteroid
                    setDead(true);              // kill me
                    soundList.get(SoundType.Explode.ordinal()).play(1.0f);
                    gameScreen.setScore(gameScreen.getScore() + 1);
                }
            }
        }
    }

    @Override
    public int getWidth() { return WIDTH; }

    @Override
    public int getHeight() { return HEIGHT; }

    @Override
    public GameObject.Type getType() { return Type.Projectile; }
}
