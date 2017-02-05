package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Graphics;


/**
 * Created by Mus on 1/24/2017.
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
                    height = 160;
                    width = 160;
                    break;
                case Large2:
                    anim.addFrame(Assets.largeRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    height = 160;
                    width = 160;
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
        g.drawImage(anim.getImage(), x, y);
        drawBounds(g);
    }

    @Override
    public void update(float deltaTime)
    {
        anim.update(15);

        speedY = Background.speedY * 2;
        y += speedY;

        if (y>GameScreen.gameHeight)
        {
            setDead(true);
            if (gameScreen.getScore() > 0)
                gameScreen.setScore(gameScreen.getScore()-1);
        }

        updateBounds(getWidth(), getHeight());
    }

    //
    // update bounds based on x,y and width, height
    // assume x,y is upper left.
    // In this case, the asteroid images are twice as big as needed, with the rock in the center
    //
    @Override
    public void updateBounds(int w, int h)
    {
        int left = x+width/2;
        int top = y+height/2;

        if (type == Type.Large1 || type == Type.Large2)
        {
            // special case for large rocks
            left = x+90;
            top = y+30;
        }
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
