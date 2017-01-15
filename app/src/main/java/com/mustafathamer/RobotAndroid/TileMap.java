package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Graphics;

import java.util.ArrayList;
import java.util.Scanner;

import static android.R.attr.width;

/**
 * Created by Mus on 1/15/2017.
 */

public class TileMap
{
    final private int numHorizTiles = 12;
    final private int numVertTiles = 300;

    final private int tileMapWidth = numHorizTiles * Tile.width;       // 480 pixels
    final private int tileMapHeight = numVertTiles * Tile.height;      // 2400 pixels

    private ArrayList tilearray = new ArrayList();

    public TileMap()
    {

    }

    public void load()
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read lines from tile map txt file
        Scanner scanner = new Scanner(RobotAndroidGame.map);
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();

            // no more lines to read
            if (line == null)
            {
                break;
            }

            if (!line.startsWith("!"))
            {
                lines.add(line);
                width = Math.max(width, line.length());

            }
        }

        // read tile characters from each line
        height = lines.size();
        for (int j = 0; j < height; j++)
        {
            String line = (String) lines.get(j);
            for (int i = 0; i < width; i++)
            {
                if (i < line.length())
                {
                    char ch = line.charAt(i);
                    // TODO fix tile ids
//                    Tile t = new Tile(i, j, Character.getNumericValue(ch));
                    Tile t = new Tile(j, i, Character.getNumericValue(ch));
                    tilearray.add(t);
                }
            }
        }

    }

    // move all tiles
    public void update()
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
