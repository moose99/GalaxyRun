package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;

import static com.mustafathamer.RobotAndroid.Asteroid.Type.Large2;

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

    private Type type;

    public Asteroid(Type t)
    {
        type = t;
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
                    break;
                case Large2:
                    anim.addFrame(Assets.largeRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    break;
                case Medium1:
                    anim.addFrame(Assets.mediumRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                    break;
                case Medium2:
                    anim.addFrame(Assets.mediumRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    break;
                case Small1:
                    anim.addFrame(Assets.smallRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                    break;
                case Small2:
                    anim.addFrame(Assets.smallRock2[i], (int) (1000.0 / Assets.numAsteroidImages));
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(anim.getImage(), x, y);
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
        }
    }
}
