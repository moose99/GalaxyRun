package com.mustafathamer.GalaxyRun;

import com.mustafathamer.framework.Graphics;

/**
 * Created by Mus on 1/24/2017.
 * x, y position specifies the center of the object
 */

public class Asteroid extends GameObject
{
    public enum Size
    {
        Large1,
        Large2,
        Medium1,
        Medium2,
        Small1,
        Small2
    }

    private int height, width;
    private Size size;          // rock size s/m/l
    private GameScreen gameScreen;

    @Override    public int getWidth()
    {
        return width;
    }
    @Override    public int getHeight()
    {
        return height;
    }
    @Override    public GameObject.Type getType() { return GameObject.Type.Asteroid; }

    //
    //
    //
    public Asteroid(GameScreen game, Size t)
    {
        size = t;
        gameScreen = game;
    }

    @Override
    public void initAssets()
    {
        for (int i = 0; i < Assets.numAsteroidImages; i++)
        {
            switch (size)
            {
                case Large1:
                    anim.addFrame(Assets.largeRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 150;
                    width = 150;
                    speedY = Background.speedY * 2;
                    break;
                case Large2:
                    anim.addFrame(Assets.largeRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 150;
                    width = 150;
                    speedY = Background.speedY * 2;
                    break;
                case Medium1:
                    anim.addFrame(Assets.mediumRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 60;
                    width = 60;
                    speedY = Background.speedY * 3;
                    break;
                case Medium2:
                    anim.addFrame(Assets.mediumRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 60;
                    width = 60;
                    speedY = Background.speedY * 3;
                    break;
                case Small1:
                    anim.addFrame(Assets.smallRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 32;
                    width = 32;
                    speedY = Background.speedY * 4;
                    break;
                case Small2:
                    anim.addFrame(Assets.smallRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 32;
                    width = 32;
                    speedY = Background.speedY * 4;
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics g)
    {
        // draw image so it is centered at x, y
        g.drawImage(anim.getImage(), Math.round(x - anim.getImage().getWidth()/2.f), Math.round(y - anim.getImage().getHeight()/2.f));
        drawBounds(g);
    }

    @Override
    public void update(float deltaTime)
    {
        anim.update(15);

        y += speedY;
        x += speedX;

        updateBounds();
        bounceOffWalls();
        checkIfOffScreen();
    }

    public Size getSize()    { return size;    }

    public Size getSmallerSize()
    {
        switch(size)
        {
            case Large1:    return Asteroid.Size.Medium1;
            case Large2:    return Asteroid.Size.Medium2;
            case Medium1:   return Asteroid.Size.Small1;
            case Medium2:   return Asteroid.Size.Small2;
            default:        return null;
        }
    }
}
