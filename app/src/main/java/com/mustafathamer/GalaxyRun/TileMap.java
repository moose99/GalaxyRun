package com.mustafathamer.GalaxyRun;

import com.mustafathamer.framework.Graphics;

import java.util.ArrayList;

/**
 * Created by Mus on 1/15/2017.
 * Creates a simple bgnd tile map with left and right edge tiles
 */

public class TileMap
{
    final static private int numHorizTiles = 15;
    final static private int numVertTiles = 300;

    final static public int mapWidth = numHorizTiles * Tile.width;       // 480 pixels
    final static public int mapHeight = numVertTiles * Tile.height;      // 9600 pixels

    private ArrayList tilearray = new ArrayList();

    public TileMap()
    {

    }

    public void load()
    {
        for (int i = 0; i < numVertTiles; i++)
        {
            Tile t = new Tile(0, i, 1 /* left */);
            tilearray.add(t);

            t = new Tile(numHorizTiles-1, i, 2 /* right */);
            tilearray.add(t);
        }
    }

    // move all tiles
    public void update(float deltaTime)
    {
        for (int i = 0; i < tilearray.size(); i++)
        {
            Tile t = (Tile) tilearray.get(i);
            t.update();
        }
    }

    public void draw(Graphics g)
    {
        for (int i = 0; i < tilearray.size(); i++)
        {
            Tile t = (Tile) tilearray.get(i);
            if (t.type != 0)
            {
                g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY());
            }
        }
    }
}
