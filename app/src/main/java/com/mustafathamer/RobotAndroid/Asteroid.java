package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Game;
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
        Large3,
        Large4,
        Large5,
        Large6,
        Large7
    }
    private Type type;

    public Asteroid(Type t)
    {
        type = t;
    }

    @Override
    public void initAssets()
    {
        switch(type)
        {
            case Large1:
                for (int i = 0; i < Assets.numAsteroidImages; i++)
                {
                    anim.addFrame(Assets.largeRock1[i], (int) (1000.0 / Assets.numAsteroidImages));
                }
                break;
        }
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(anim.getImage(), x, y);
    }

    @Override
    public void update()
    {
        anim.update(15);
    }
}
