package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Graphics;


/**
 * Created by Mus on 1/24/2017.
 * x, y position specifies the center of the object
 */

public class Asteroid extends GameObject
{
    static public enum Type
    {
        Large1,
        Large2,
        Medium1,
        Medium2,
        Small1,
        Small2
    }

    private int height, width;
    private Type type;
    private GameScreen gameScreen;

    public Asteroid(GameScreen game, Type t)
    {
        type = t;
        gameScreen = game;
    }

    @Override
    public void initAssets()
    {
        for (int i = 0; i < Assets.numAsteroidImages; i++)
        {
            switch (type)
            {
                case Large1:
                    anim.addFrame(Assets.largeRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 150;
                    width = 150;
                    break;
                case Large2:
                    anim.addFrame(Assets.largeRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 150;
                    width = 150;
                    break;
                case Medium1:
                    anim.addFrame(Assets.mediumRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 60;
                    width = 60;
                    break;
                case Medium2:
                    anim.addFrame(Assets.mediumRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 60;
                    width = 60;
                    break;
                case Small1:
                    anim.addFrame(Assets.smallRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 32;
                    width = 32;
                    break;
                case Small2:
                    anim.addFrame(Assets.smallRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 32;
                    width = 32;
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics g)
    {
        // draw image so it is centered at x, y
        g.drawImage(anim.getImage(), x - anim.getImage().getWidth()/2, y - anim.getImage().getHeight()/2);
        drawBounds(g);
    }

    @Override
    public void update(float deltaTime)
    {
        anim.update(15);

        speedY = Background.speedY * 2;
        y += speedY;
        x += speedX;

        updateBounds(getWidth(), getHeight());

        // bounce off the walls
        int border = 32;
        if ( (getBounds().right >= GameScreen.gameWidth - border) || getBounds().left <= border )
        {
            speedX = -speedX;
        }

        // go off the bottom of the screen
        if (y>GameScreen.gameHeight)
        {
            setDead(true);
            if (gameScreen.getScore() > 0)
                gameScreen.setScore(gameScreen.getScore()-1);
        }
    }

    //
    // update bounds based on x,y and width, height
    // assume x,y is center of the rock
    // In this case, the asteroid images are ~twice as big as needed, with the rock in the center
    //
    @Override
    public void updateBounds(int w, int h)
    {
        int left = x - w/2;
        int top = y - h/2;
        bounds.set(left, top, left + w, top + h);
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public GameObject.Type getType() { return GameObject.Type.Asteroid; }
}
