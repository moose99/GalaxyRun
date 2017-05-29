package com.mustafathamer.GalaxyRun;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.mustafathamer.util.Assert;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.util.Vector2D;

import java.util.Vector;


/**
 * Created by Mus on 11/26/2016.
 */

public class AlienProjectile extends GameObject
{
    private GameScreen gameScreen;
    private Rect laserRect;
    private Player player = GameScreen.getPlayer();
    private Vector2D dir;
    private float rotateDeg;

    // Sounds identifiers
    public enum SoundType
    {
        Explode
    }

    public AlienProjectile(GameScreen game)
    {
        speedY = 7;
        speedX = 7;

        this.gameScreen = game;
    }

    @Override
    public void initAssets()
    {
        // add sounds using order of SoundType enum
        soundList.add(Assets.explosion);

        laserRect = Assets.ssReduxSprites.getRect("laserBlue03.png");
        Assert.Assert(laserRect != null);
    }

    public void calcDirection()
    {
        Vector2D ctr = new Vector2D(getBounds().centerX(), getBounds().centerY());
        Vector2D playerCtr = new Vector2D(player.getBounds().centerX(), player.getBounds().centerY());
        dir = playerCtr.subtract(ctr);
        dir.normalize();

        Vector2D yAxis = new Vector2D(0, 1);
        double dot = yAxis.dotProduct(dir);
        rotateDeg = (float)Math.toDegrees(Math.acos(dot));
        if (dir.getX() > 0)     // check if we need to rotate left vs right
            rotateDeg = -rotateDeg;
    }

    //
    // Moves upwards until it goes off the screen
    @Override
    public void update(float deltaTime)
    {
        int speed=2;
        x += Math.round(dir.getX() * speed);
        y += Math.round(dir.getY() * speed);

        updateBounds();
        if (y < 0)
        {
            setDead(true);
        }
        else
        {
            checkCollision();
        }

        bounceOffWalls();
        checkBelowScreen();
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawRect(x, y, getWidth(), getHeight(), Color.BLUE);

        /*
        g.drawImage(Assets.ssReduxSprites.getImage(),           // image
                x, y,                                           // translation
//                rotateDeg,                                      // rotation
                laserRect.left, laserRect.top,                  // srcx, srcy
                laserRect.width(), laserRect.height());         // width, height
*/

        drawBounds(g);
    }

    //
    // check for collisions with player
    //
    private void checkCollision()
    {
        for (int i=0; i<gameScreen.getGameObjects().size(); i++)
        {
            GameObject gameObject = gameScreen.getGameObjects().get(i);
            if (gameObject.getType() == Type.Player)
            {
                if (gameObject.getBounds().intersect(getBounds()))
                {
                    setDead(true);              // kill me
                    player.wasShot();
                }
            }
        }
    }

    @Override
    public int getWidth() { return 4; }     // laserRect.width(); }

    @Override
    public int getHeight() { return 4; }    // laserRect.height(); }

    @Override
    public GameObject.Type getType() { return Type.AlienProjectile; }
}
