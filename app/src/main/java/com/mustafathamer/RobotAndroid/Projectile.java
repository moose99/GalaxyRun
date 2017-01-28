package com.mustafathamer.RobotAndroid;

import android.graphics.Color;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;

/**
 * Created by Mus on 11/26/2016.
 */

public class Projectile extends GameObject
{
    private static final int WIDTH = 4;
    private static final int HEIGHT = 4;

    public Projectile(int startX, int startY)
    {
        x = startX;
        y = startY;
        speedY = 7;
        visible = true;
    }

    @Override
    public void update(float deltaTime)
    {
        y -= speedY;
        bounds.set(x, y, x + WIDTH, y + HEIGHT);
        if (y < 0)
        {
            visible = false;
            bounds = null;
        }

        if (y > 0)
        {
            checkCollision();
        }
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawRect(x, y, WIDTH, HEIGHT, Color.YELLOW);
    }

    private void checkCollision()
    {

    }


}
