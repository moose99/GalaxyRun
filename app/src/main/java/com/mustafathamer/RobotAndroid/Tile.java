package com.mustafathamer.RobotAndroid;

import android.graphics.Rect;
import com.mustafathamer.framework.Image;


/**
 * Created by Mus on 11/26/2016.
 * Vertically scrolling tiles, scrolls downward (positive Y)
 */

public class Tile
{

    private int tileX, tileY, speedY;
    public int type;
    public Image tileImage;

    private Player player = GameScreen.getPlayer();
    final static public int width = 32;
    final static public int height = 32;

    private Rect r;

    public Tile(int x, int y, int typeInt)
    {
        tileX = x * width;
        tileY = -TileMap.mapHeight + y * height;

        type = typeInt;

        r = new Rect();
        switch(type)
        {
            case 1:
                tileImage = Assets.tileLeft;
                break;
            case 2:
                tileImage = Assets.tileRight;
                break;
            default:
                type = 0;
                break;
        }
    }

    public void update()
    {
        speedY = Background.speedY * 2;
        tileY += speedY;

        // vertical wrap
        if (tileY > GameScreen.gameHeight)
            tileY -= TileMap.mapHeight;

        r.set(tileX, tileY, tileX + width, tileY + height);


        if (Rect.intersects(r, Player.bounds) && type != 0)
        {
            player.crash();
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

}
