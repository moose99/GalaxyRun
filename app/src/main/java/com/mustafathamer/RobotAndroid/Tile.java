package com.mustafathamer.RobotAndroid;

import android.graphics.Rect;

import com.mustafathamer.framework.Image;

//import static android.R.attr.width;
//import static android.support.v4.media.session.MediaControllerCompatApi21.TransportControls.play;

/**
 * Created by Mus on 11/26/2016.
 * Vertically scrolling tiles, scrolls downward (positive Y)
 */

public class Tile
{

    private int tileX, tileY, speedY;
    public int type;
    public Image tileImage;

    private Robot robot = GameScreen.getRobot();
    private Background bg = GameScreen.getBg1();
    final private int width = 40;
    final private int height = 40;
    final private int tileMapWidth = 12*width;
    final private int tileMapHeight = 300*height;
    private Rect r;

    public Tile(int x, int y, int typeInt)
    {
        tileX = x * width;
        tileY = -tileMapHeight + y * height;

        type = typeInt;

        r = new Rect();

        /*
        if (type == 5)
        {
            tileImage = Assets.tiledirt;
        } else if (type == 8)
        {
            tileImage = Assets.tilegrassTop;
        } else if (type == 4)
        {
            tileImage = Assets.tilegrassLeft;
        } else if (type == 6)
        {
            tileImage = Assets.tilegrassRight;

        } else if (type == 2)
        {
            tileImage = Assets.tilegrassBot;
        } else
        {
            type = 0;
        }
*/
        switch(type)
        {
            case 5:
                tileImage = Assets.tiledirt;
                break;
            case 8:
                tileImage = Assets.tilegrassRight;
                break;
            case 2:
                tileImage = Assets.tilegrassLeft;
                break;
            case 4:
                tileImage = Assets.tilegrassTop;
                break;
            case 6:
                tileImage = Assets.tilegrassBot;
                break;
            default:
                type = 0;
                break;
        }
    }

    public void update()
    {
        speedY = bg.getSpeedY() * 3;
        tileY += speedY;

        // vertical wrap
        if (tileY > GameScreen.gameHeight)
            tileY -= tileMapHeight;

        r.set(tileX, tileY, tileX + 40, tileY + 40);


        if (Rect.intersects(r, Robot.bounds) && type != 0)
        {
            robot.crash();
        }
    }

    public int getTileX()
    {
        return tileX;
    }

    public void setTileX(int tileX)
    {
        this.tileX = tileX;
    }

    public int getTileY()
    {
        return tileY;
    }

    public void setTileY(int tileY)
    {
        this.tileY = tileY;
    }

    public Image getTileImage()
    {
        return tileImage;
    }

    public void setTileImage(Image tileImage)
    {
        this.tileImage = tileImage;
    }

    /*
    public void checkVerticalCollision(Rect rtop, Rect rbot)
    {
        if (Rect.intersects(rtop, r))
        {

        }

        if (Rect.intersects(rbot, r) && type == 8)
        {
            robot.setSpeedY(0);
            robot.setCenterY(tileY - 63);
        }
    }

    public void checkSideCollision(Rect rleft, Rect rright, Rect leftfoot, Rect rightfoot)
    {
        if (type != 5 && type != 2 && type != 0)
        {
            if (Rect.intersects(rleft, r))
            {
                robot.setCenterX(tileX + 102);

                robot.setSpeedX(0);

            } else if (Rect.intersects(leftfoot, r))
            {

                robot.setCenterX(tileX + 85);
                robot.setSpeedX(0);
            }

            if (Rect.intersects(rright, r))
            {
                robot.setCenterX(tileX - 62);

                robot.setSpeedX(0);
            } else if (Rect.intersects(rightfoot, r))
            {
                robot.setCenterX(tileX - 45);
                robot.setSpeedX(0);
            }
        }
    }
    */

}
